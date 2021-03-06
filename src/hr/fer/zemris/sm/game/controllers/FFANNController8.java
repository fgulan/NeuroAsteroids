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
 * This neural network will accept as an input:
 * <ul>
 *     <li>Ships velocity</li>
 *     <li>Angle of the closest star relative to the ship in interval (-1, 1)</li>
 *     <li>Distance to the closest star relative to the ship center</li>
 *     <li>Amount of fuel it has left on the scale from 0 to 1</li>
 *     <li>Amount of ammo it has left on the scale from 0 to 1</li>
 *     <li>Binary information that tells it if the asteroid is on his LEFT, RIGHT or BACK side. Where sides are defined as:</li>
 *     <ul>
 *         <li>LEFT: -150 to 0 degrees</li>
 *         <li>RIGHT: 0 to 150 degrees</li>
 *         <li>BACK: -180 to -150 and 150 to 180 degrees</li>
 *     </ul>
 * </ul>
 *
 * This controller will only work if there is at least one star in the world
 *
 * Created by Fredi Šarić on 12.01.16.
 */
public class FFANNController8 extends AbstractPhenotypeController {
    /*
    * These controller needs to have at least one star(preferably one star) and one asteroid
    */
    private static final int VELOCITY_INDEX = 0;
    private static final int STAR_ANGEL = 1;
    private static final int STAR_DISTANCE = 2;
    private static final int FUEL_AMOUNT = 3;
    private static final int AMMO_AMOUNT = 4;
    private static final int RIGHT_SIDE_DANGER = 5;
    private static final int LEFT_SIDE_DANGER  = 6;
    private static final int BACK_SIDE_DANGER  = 7;

    private static final double RIGHT_ANGEL = 150;
    private static final double LEFT_ANGEL = -150;

    private static final double MAX_DANGER_DISTANCE = 60;
    private static final double MIN_DANGER_DISTANCE = 240;    //Space needed to fit 3 Asteroids

    private List<Input> inputList;

    private double[] netInput;

    private DistanceComparator dComp;

    public FFANNController8(IPhenotype phenotype) {
        super(phenotype);
        inputList = new ArrayList<>(4);
        netInput = new double[8];
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

        netInput[VELOCITY_INDEX] = ship.getVelocity().norm();
        netInput[STAR_ANGEL] = calculateAngle(ship, closestStar) / 180;
        netInput[STAR_DISTANCE] = calcDistance(ship, closestStar);

        netInput[FUEL_AMOUNT] = world.getFuelLeft() / FUEL_START;
        netInput[AMMO_AMOUNT] = world.getMissilesLeft() / NUMBER_OF_MISSILES;

        netInput[RIGHT_SIDE_DANGER] = rDist < MIN_DANGER_DISTANCE ? calculateDanger(rDist) : 0;
        netInput[LEFT_SIDE_DANGER]  = lDist < MIN_DANGER_DISTANCE ? calculateDanger(lDist) : 0;
        netInput[BACK_SIDE_DANGER]  = bDist < MIN_DANGER_DISTANCE ? calculateDanger(bDist) : 0;

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
        double res = 1 - ((dist - MAX_DANGER_DISTANCE)  / (MIN_DANGER_DISTANCE - MAX_DANGER_DISTANCE));
        return res > 1 ? 1 : res;
    }

    private static class DistanceComparator implements Comparator<Sprite>, Serializable{

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
