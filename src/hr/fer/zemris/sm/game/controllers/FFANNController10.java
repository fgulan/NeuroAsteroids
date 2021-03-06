package hr.fer.zemris.sm.game.controllers;

import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.game.Constants;
import hr.fer.zemris.sm.game.managers.SpriteManager;
import hr.fer.zemris.sm.game.models.Ship;
import hr.fer.zemris.sm.game.models.Sprite;
import hr.fer.zemris.sm.game.models.Star;
import hr.fer.zemris.sm.game.physics.IVector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller provides neural network with this information:
 * <ul>
*     <li>Binary information of the star angle relative to the ship orientation. Left = -1, Right = 1</li>
 *     <li>Information how much frames before did ship fired missile on scale 0-1 linearly</li>
 *     <li>Amount of danger form the right, left, and front.</li>
 *     <ul>
 *         <li>Where sides are defined as:</li>
 *         <ul>
 *             <li>Front view is a rectangle surface of width 120 length units (relative to the screen) and unlimited length</li>
 *             <li>Right is everything to the right of the ship</li>
 *             <li>Left  is everything to the left  of the ship</li>
 *         </ul>
 *         <li>And amount of danger is defined as:</li>
 *         <ul>
 *             <li>0 - 60 length units = 1 danger</li>
 *             <li>60 - 120 length units = 3/4 danger</li>
 *             <li>120 - 180 length units = 1/2 danger</li>
 *             <li>180 - 240 length units = 1/4 danger</li>
 *             <li>240 &gt length units = danger</li>
 *         </ul>
 *     </ul>
 * </ul>
 *
 * This controller will only work if there is at least one star in the world
 *
 * Created by Fredi Šarić on 14.01.16.
 */
public class FFANNController10 extends AbstractPhenotypeController {

    private static final int STAR_ANGLE = 0;
    private static final int LEFT_DANGER = 1;
    private static final int RIGHT_DANGER = 2;
    private static final int FRONT_DANGER = 3;
    private static final int AMMO_COUNT = 4;
    private static final int FIRED_BEFORE = 5;

    private static final double FRONT_VIEW_WIDTH = 120; //From each side

    private static final double MAX_DANGER_DISTANCE = 60;
    private static final double ONE_ASTEROID_DISTANCE = 120;
    private static final double TWO_ASTEROID_DISTANCE = 180;
    private static final double MIN_DANGER_DISTANCE = 240;    //Space needed to fit 3 Asteroids

    private static final double FIRED_BEFORE_FRAME_SPAN = 30;

    private List<Input> inputList;
    private double[] netInput;
    private int firedBeforeCounter;

    public FFANNController10(IPhenotype phenotype) {
        super(phenotype);
        inputList = new ArrayList<>(4);
        netInput = new double[6];
        firedBeforeCounter = 0;
    }

    @Override
    public List<Input> getInput() {
        SpriteManager sm = world.getSpriteManager();
        Ship ship = sm.getShip();

        //Will only work if there is at least one star in the world
        Star closestStar = sm.getStars().stream().min((o1,o2) -> (int)Math.signum(calcDistance(ship, o1)-calcDistance(ship, o2))).get();

        MutableDouble closestLeft  = new MutableDouble(Double.MAX_VALUE);
        MutableDouble closestRight = new MutableDouble(Double.MAX_VALUE);
        MutableDouble closestFront = new MutableDouble(Double.MAX_VALUE);

        sm.getAsteroids().forEach( a -> {       //calculate left, right and front danger
            double angle = calculateAngle(ship, a);
            double distance = calcDistance(ship, a);

            if(angle > 0 && angle < 180) { //from the right
                if(closestRight.getNum() > distance) {
                    closestRight.setNum(distance);
                }
            } else {
                if (closestLeft.getNum() > distance) {
                    closestLeft.setNum(distance);
                }
            }
            if(angle > -90 && angle < 90) { //front danger is distance in y direction ( in ships frame of reference)
                IVector vec = ship.getCenter().nSub(a.getCenter());
                double yDist = vec.get(1);
                double xDist = vec.get(0);
                if(Math.abs(xDist) < FRONT_VIEW_WIDTH && closestFront.getNum() > yDist) {
                    closestFront.setNum(calculateDanger(yDist));
                }
            }
        });

        netInput[STAR_ANGLE]   = Math.signum(calculateAngle(ship, closestStar));
        netInput[LEFT_DANGER]  = calculateDanger(closestLeft.getNum());
        netInput[RIGHT_DANGER] = calculateDanger(closestRight.getNum());
        netInput[FRONT_DANGER] = calculateDanger(closestFront.getNum());
        netInput[AMMO_COUNT]   = ((double)world.getMissilesLeft())/ Constants.NUMBER_OF_MISSILES;
        netInput[FIRED_BEFORE] = hasFiredBefore();  //1 if it has -1 otherwise

        double[] output = phenotype.work(netInput);
        inputList.clear();

        if(output[0] > 0.5) {
            inputList.add(Input.MOVE);
        }
        if(output[1] > 0.5) {
            inputList.add(Input.LEFT);
        }
        if(output[2] > 0.5) {
            inputList.add(Input.RIGHT);
        }
        if(output[3] > 0.5) {
            inputList.add(Input.FIRE);
            firedBeforeCounter++;
        }

        return inputList;
    }

    private double hasFiredBefore() {
        if(firedBeforeCounter < FIRED_BEFORE_FRAME_SPAN) return 1; //IF fired before in last time span
        firedBeforeCounter = 0; //reset counter
        return -1;
    }

    /*
  * Danger is 1 if distance is smaller than MAX_DANGER_DISTANCE
  * or 0 if distance greater than MIN_DANGER_DISTANCE
  */
    private double calculateDanger(double dist) {
        if(dist < MAX_DANGER_DISTANCE) return 1;
        if(dist < ONE_ASTEROID_DISTANCE) return 3/4.0;
        if(dist < TWO_ASTEROID_DISTANCE) return 2/4.0;
        if(dist < MIN_DANGER_DISTANCE) return 1/4.0;
        return 0;
    }



    private static class MutableDouble implements Serializable { //just in case
        private Double num;

        public MutableDouble(Double num) {
            this.num = num;
        }

        public Double getNum() {
            return num;
        }

        public void setNum(Double num) {
            this.num = num;
        }
    }
}
