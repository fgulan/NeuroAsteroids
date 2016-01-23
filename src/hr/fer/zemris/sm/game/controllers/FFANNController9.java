package hr.fer.zemris.sm.game.controllers;

import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.game.managers.SpriteManager;
import hr.fer.zemris.sm.game.models.Ship;
import hr.fer.zemris.sm.game.models.Sprite;
import hr.fer.zemris.sm.game.physics.IVector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static hr.fer.zemris.sm.game.Constants.FUEL_START;
import static hr.fer.zemris.sm.game.Constants.NUMBER_OF_MISSILES;

/**
 * This controller gives all the same inputs to the network as the
 * {@link FFANNController8} except that it does not give distance to the
 * closes star relative to the ship center and it does not provide information
 * of the ship velocity. Also information of the star angle is now binary eg. if
 * the star is to the left than input is -1, and if the star is to the right of the
 * ship than input is 1.
 *
 * This controller will only work if there is at least one star in the world
 *
 * Created by Fredi Šarić on 14.01.16.
 */
public class FFANNController9 extends AbstractPhenotypeController {

    /*
     * These controller needs to have at least one star(preferably one star) and one asteroid
     */
    private static final int STAR_ANGEL = 0;
    private static final int FUEL_AMOUNT = 1;
    private static final int AMMO_AMOUNT = 2;
    private static final int RIGHT_SIDE_DANGER = 3;
    private static final int LEFT_SIDE_DANGER  = 4;
    private static final int BACK_SIDE_DANGER  = 5;

    private static final double RIGHT_ANGEL = 150;
    private static final double LEFT_ANGEL = -150;

    private static final double MAX_DANGER_DISTANCE = 60;
    private static final double ONE_ASTEROID_DISTANCE = 120;
    private static final double TWO_ASTEROID_DISTANCE = 180;
    private static final double MIN_DANGER_DISTANCE = 240;    //Space needed to fit 3 Asteroids


    private double[] netInput;
    private List<Input> inputList;
    private DistanceComparator dComp;


    public FFANNController9(IPhenotype phenotype) {
        super(phenotype);
        inputList = new ArrayList<>(4);
        netInput = new double[6];
        dComp = new DistanceComparator();
    }

    @Override
    public List<Input> getInput() {
        SpriteManager sm = world.getSpriteManager();
        Ship ship = sm.getShip();
        dComp.setShip(ship);
        Sprite closestStar = sm.getStars().stream().min(dComp).get();
        dComp.setShip(null);

        double rDist = Double.MAX_VALUE;
        double lDist = Double.MAX_VALUE;
        double bDist = Double.MAX_VALUE;

        for(Sprite asteroid : sm.getAsteroids()) {
            double dist = calcDistance(ship, asteroid);
            double angel =  calculateAngle(ship, asteroid);

            if(angel >= 0 && angel < RIGHT_ANGEL) {
                if(dist < rDist) { rDist = dist; }
            } else if(angel < 0 && angel > LEFT_ANGEL) {
                if(dist < lDist) { lDist = dist; }
            } else {
                if(dist < bDist) { bDist = dist; }
            }
        }

        netInput[STAR_ANGEL] = Math.signum(calculateAngle(ship, closestStar));

        netInput[FUEL_AMOUNT] = world.getFuelLeft() / FUEL_START;
        netInput[AMMO_AMOUNT] = world.getMissilesLeft() / NUMBER_OF_MISSILES;

        netInput[RIGHT_SIDE_DANGER] = calculateDanger(rDist);
        netInput[LEFT_SIDE_DANGER]  = calculateDanger(lDist);
        netInput[BACK_SIDE_DANGER]  = calculateDanger(bDist);


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
        }

        return inputList;
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

    private static class DistanceComparator implements Comparator<Sprite>, Serializable {

        Ship ship;

        void setShip(Ship ship) {
            this.ship =  ship;
        }

        @Override
        public int compare(Sprite o1, Sprite o2) {
            double d1 = calcDistance(ship, o1);
            double d2 = calcDistance(ship, o2);

            return (int)Math.signum(d1-d2);
        }
    }
}
