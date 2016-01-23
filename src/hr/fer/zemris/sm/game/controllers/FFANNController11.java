package hr.fer.zemris.sm.game.controllers;

import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.game.managers.SpriteManager;
import hr.fer.zemris.sm.game.models.Asteroid;
import hr.fer.zemris.sm.game.models.Ship;
import hr.fer.zemris.sm.game.models.Sprite;
import hr.fer.zemris.sm.game.models.Star;
import hr.fer.zemris.sm.game.physics.IVector;
import hr.fer.zemris.sm.game.world.GameEvent;
import hr.fer.zemris.sm.game.world.GameWorld;

import static hr.fer.zemris.sm.game.Constants.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This controller provides neural network with this information:
 * <ul>
 *     <li>Binary information of the star angle relative to the ship orientation. Left = -1, Right = 1</li>
 *     <li>
 *         Whether or not there is an asteroid in ships field of view. Field of view is a rectangle
 *         of width same as ship and length that is equal as 3 * asteroid diameter and it is positioned
 *         in front of the ship
 *     </li>
 *     <li>
 *         Information of the number of frames that have passed since last shoot have been taken. That information
 *         is modeled by exponential function that starts from 1 and goes to 0.
 *     </li>
 *     <li>Information of how much danger there is from the LEFT and RIGHT. That information
 *         is modeled by exponential function that starts from 1 and goes to 0 and it depends on distance from
 *         asteroid center to ship center.
 *     </li>
 * </ul>
 *
 *
 * This controller will only work if there is at least one star in the world
 *
 * Created by Fredi Šarić on 15.01.16.
 */
public class FFANNController11 extends AbstractPhenotypeController {

    public static final long serialVersionUID = 7261276772547929496L;

    //Input indexes
    public static final int STAR_ANGLE = 0;
    public static final int RIGHT_DANGER = 1;
    public static final int LEFT_DANGER  = 2;
    public static final int FIELD_VIEW = 3;
    public static final int AMMO_FRESHNESS = 4;

    private static final double RIGHT = 1;
    private static final double LEFT = -1;

    private static final double PRESENT = 1;
    private static final double NOT_PRESENT = -1;

    //This will be y will bi subtracted by 1 so this is actually 1
    //This is needed so that input in network will be in range of
    // (-1, 1)
    private static final double TOP_FUN_VAL =  2;

    //Danger distances
    private static final double MAX_DANGER_DISTANCE = 60;
    private static final double MIN_DANGER_DISTANCE = 300;
    private static final double DANGER_SLOPE = 0.05;

    //Firing freshness
    private static final double MIN_FIRING_FRESHNESS = MISSILE_CHARGE_TIME/((double)MISSILE_CHARGE_DELTA);      //20 frames
    private static final double MAX_FIRING_FRESHNESS = MISSILE_CHARGE_TIME/((double)MISSILE_CHARGE_DELTA) * 3;  //60 frames
    private static final double FIRING_FRESHNESS_SLOPE = 0.005;


    //Field view parameters, field view needs to be narrow so that ship doesn't shoot pointlessly
    private static final double FIELD_VIEW_RADIUS = 30; //The same as the ship
    private static final double FIELD_VIEW_LENGTH = 4 * FIELD_VIEW_RADIUS;  //3 lengths of ship radius in front of it

    private double[] netInput;
    private List<Input> inputList;
    private int framesSinceFired;
    private int lastSeenNumOfFiredMissiles;
    private int numOfFiredMissiles;

    public FFANNController11(IPhenotype phenotype) {
        super(phenotype);

        netInput = new double[5];
        inputList = new ArrayList<>(4);
        framesSinceFired = 0;
        numOfFiredMissiles = 0;
        lastSeenNumOfFiredMissiles = 0;
    }

    @Override
    public List<Input> getInput() {
        SpriteManager sm = world.getSpriteManager();
        Ship ship = sm.getShip();

        //Closest star
        Star star = sm.getStars().stream().min((s1,s2) -> (int)Math.signum(calcDistance(ship, s1) - calcDistance(ship,s2))).get();

        double leftDanger   = -1;   //This will be from -1 to 1
        double rightDanger  = -1;   //This will be from -1 to 1
        double frontPresent = NOT_PRESENT;   //This will be -1 or 1

        for (Asteroid asteroid : sm.getAsteroids()) {
            double angle = calculateAngle(ship, asteroid);
            double distance = calcDistance(ship, asteroid);

            double danger = exponentialFunction(TOP_FUN_VAL, DANGER_SLOPE,                 //Top, bottom
                                                distance,                                  // X
                                                MAX_DANGER_DISTANCE, MIN_DANGER_DISTANCE); // X_MIN, X_MAX

            if(angle > 0 && rightDanger > danger) { //Right
                rightDanger = danger;
            } else if(leftDanger > danger) {        //Left
                leftDanger = danger;
            }

            if(angle > -90 && angle < 90) { //If it might be in view field
                IVector vec = ship.getCenter().nSub(asteroid.getCenter());
                double xDist = vec.get(0);
                double yDist = vec.get(1);
                if(xDist < FIELD_VIEW_RADIUS && yDist < FIELD_VIEW_LENGTH) {
                    frontPresent = PRESENT;
                }
            }
        }

        updateSinceFired();

        netInput[STAR_ANGLE]     = calculateAngle(ship, star) >= 0 ? RIGHT : LEFT;
        netInput[RIGHT_DANGER]   = rightDanger;
        netInput[LEFT_DANGER]    = leftDanger;
        netInput[FIELD_VIEW]     = frontPresent;
        netInput[AMMO_FRESHNESS] = exponentialFunction(TOP_FUN_VAL, FIRING_FRESHNESS_SLOPE,
                                                       framesSinceFired,
                MIN_FIRING_FRESHNESS, MAX_FIRING_FRESHNESS);

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

    private void updateSinceFired() {
        if(numOfFiredMissiles != lastSeenNumOfFiredMissiles) {  //If it fired, this method is called every frame
            lastSeenNumOfFiredMissiles = numOfFiredMissiles;
            framesSinceFired = 0;
        } else {
            framesSinceFired++;
        }
    }

    private double exponentialFunction(double top, double slope, double x, double xMax, double xMin) {
        /*
         *     {    1, x < xmin
         * f = <    TOP * (TOP / BOTTOM) ^ ((x - X_MIN)/(X_MAX - X_MIN)) - 1, otherwise
         +     {
         */
        if(x < xMin) {
            return 1;
        }

        return top * Math.pow(top / slope, (x - xMin)/(xMax - xMin)) - 1;
    }

    @Override
    public void setWorld(GameWorld world) {
        super.setWorld(world);
        if(world != null) {
            world.addListener(GameEvent.MISSILE_FIRED, e -> numOfFiredMissiles++);
            numOfFiredMissiles = 0; //reset every time when world is changed
        }
    }
}
