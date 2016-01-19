package hr.fer.zemris.sm.game.controllers;

import hr.fer.zemris.sm.evolution.representation.FFANN.DoubleArrayGenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.game.managers.SpriteManager;
import hr.fer.zemris.sm.game.models.Ship;
import hr.fer.zemris.sm.game.models.Sprite;
import hr.fer.zemris.sm.game.physics.IVector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 *
 * Created by Fredi Šarić on 20.12.15.
 */
public class FFANNPhenotypeController extends AbstractPhenotypeController {

    /**
     * Inputs:
     * First:  speed of the network
     * <p>
     * Second: angle at which network sees star
     * Third:  is star distance
     * <p>
     * Fourth: angle at which network sees nearest asteroid
     * Fifth:  distance of nearest asteroid
     * Sixth:  x component of velocity of closest asteroid
     * Seventh:y component of velocity of closest asteroid
     */
    public static final int SHIP_SPEED_INDEX = 0;
    public static final int STAR_VIEW_ANGLE  = 1;
    public static final int STAR_DISTANCE    = 2;
    public static final int ASTEROID_ANGLE   = 3;
    public static final int ASTEROID_DISTANCE= 4;
    public static final int ASTEROID_VX_COMP = 5;
    public static final int ASTEROID_VY_COMP = 6;

    public static final int MOVE_INDEX  = 0;
    public static final int LEFT_INDEX  = 1;
    public static final int RIGHT_INDEX = 2;
    public static final int FIRE_INDEX  = 3;

    protected List<Input> inputList;
    protected double[] netInput;

    private int moveCounter;
    private int leftCounter;
    private int rightCounter;
    private int firecounter;

    public FFANNPhenotypeController(IPhenotype<DoubleArrayGenotype> ffann) {
        super(ffann);
        this.inputList = new ArrayList<>(4);
        this.netInput = new double[5];
    }

    @Override
    public List<Input> getInput() {
        inputList.clear();

        SpriteManager sm = world.getSpriteManager();

        Ship ship = sm.getShip();

        netInput[SHIP_SPEED_INDEX] = ship.getVelocity().norm();

        if(sm.getAsteroids().size() > 0) {
            putAsteroidInfo(sm,ship);
        } else {
            putEmptyAsteroidInfo();
        }

        if(sm.getStars().size() > 0) {
            putStarInfo(sm, ship);
        } else {
            putEmptyStarInfo();
        }

        double[] output = phenotype.work(netInput);

        //Set controller inputs
        if(output[MOVE_INDEX] > 0.5) {
            inputList.add(Input.MOVE);
        }
        if(output[LEFT_INDEX] > 0.5) {
            inputList.add(Input.LEFT);
        }
        if(output[RIGHT_INDEX] > 0.5) {
            inputList.add(Input.RIGHT);
        }
        if(output[FIRE_INDEX] > 0.5) {
            inputList.add(Input.FIRE);
        }

        return inputList;
    }

    protected void putAsteroidInfo(SpriteManager sm, Ship ship) {

        SpriteDistanceMapper mapper = new SpriteDistanceMapper(ship);
        SpriteDistance closestAsteroid = sm.getAsteroids().stream().map(mapper::apply).min(SpriteDistance.BY_DISTANCE).get();

        netInput[ASTEROID_ANGLE]    = calculateViewAngle(ship, closestAsteroid.sprite);
        netInput[ASTEROID_DISTANCE] = closestAsteroid.distance;
        IVector asteroidSpeed       = closestAsteroid.sprite.getVelocity();
        netInput[ASTEROID_VX_COMP]  = asteroidSpeed.get(0);
        netInput[ASTEROID_VY_COMP]  = asteroidSpeed.get(1);
    }

    protected void putEmptyAsteroidInfo() {
        netInput[ASTEROID_ANGLE] = 0;
        netInput[ASTEROID_DISTANCE] = 0;
        netInput[ASTEROID_VX_COMP] = 0;
        netInput[ASTEROID_VY_COMP] = 0;
    }

    protected void putStarInfo(SpriteManager sm,final Ship ship) {
        SpriteDistanceMapper mapper = new SpriteDistanceMapper(ship);
        SpriteDistance star = sm.getStars().stream().map(mapper::apply).min(SpriteDistance.BY_DISTANCE).get();

        netInput[STAR_VIEW_ANGLE] = calculateViewAngle(ship, star.sprite);
        netInput[STAR_DISTANCE]   = star.distance;
    }

    protected void putEmptyStarInfo() {
        netInput[ASTEROID_ANGLE]    = 0;
        netInput[ASTEROID_DISTANCE] = 0;
        netInput[ASTEROID_VX_COMP]  = 0;
        netInput[ASTEROID_VY_COMP]  = 0;
    }

    protected static double calculateViewAngle(Ship ship, Sprite sprite) {
        IVector ac = sprite.getCenter();
        IVector sc = ship.getCenter();

        IVector vec = ac.nSub(sc);
        double a = Math.atan2(vec.get(1), vec.get(0)); //Angle between ship and sprite

        a = a * 180.0 / Math.PI;
        a += 90;

        if ( a > 180.0 ) {
            a -= 360.0;
        }

        if ( a < -180.0) {
            a += 360.0;
        }

        double shipAngle = ship.getCurrentAngle();

        if (shipAngle > 180.0) {
            shipAngle -= 360.0;
        }

        if (shipAngle < -180.0) {
            shipAngle += 360.0;
        }

        double alpha = shipAngle - a;

        if (alpha > 180) {
            alpha -= 360;
        }

        if (alpha < -180) {
            alpha += 360;
        }
        return - alpha / 180.0;   //Normalize and - is because of orientation
    }

    private static class SpriteDistanceMapper implements Function<Sprite, SpriteDistance> {
        private Ship ship;

        public SpriteDistanceMapper(Ship ship) {
            this.ship = ship;
        }

        @Override
        public SpriteDistance apply(Sprite sprite) {
            IVector shipCenter = ship.getCenter();
            IVector spriteCenter = sprite.getCenter();

            return new SpriteDistance(sprite, shipCenter.nSub(spriteCenter).norm());
        }
    }

    private static class SpriteDistance {

        public static final Comparator<SpriteDistance> BY_DISTANCE = (s1, s2) -> (int) Math.signum(s1.distance - s2.distance);

        Sprite sprite;
        double distance;

        public SpriteDistance(Sprite sprite, double distance) {
            this.sprite = sprite;
            this.distance = distance;
        }
    }

}
