package hr.fer.zemris.sm.game.controllers;

import hr.fer.zemris.sm.evolution.representation.FFANN.DoubleArrayGenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.game.managers.SpriteManager;
import hr.fer.zemris.sm.game.models.Ship;
import hr.fer.zemris.sm.game.models.Sprite;
import hr.fer.zemris.sm.game.physics.IVector;
import hr.fer.zemris.sm.game.world.GameWorld;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Network sees in four directions, in every direction it sees the nearest sprite.
 * Asteroids are of ASTEROID_TYPE and stars are of STAR_TYPE for network input.
 * Network also sees ship velocity
 *
 * Created by Fredi Šarić on 22.12.15.
 */
public class FFANNPhenotypeController3 extends AbstractPhenotypeController {

    private static final int SPEED_INDEX = 0;
    private static final int FRONT_TYPE = 1;
    private static final int RIGHT_TYPE = 2;
    private static final int LEFT_TYPE  = 3;
    private static final int BACK_TYPE  = 4;

    private static final double ASTEROID_TYPE = -1;
    private static final double START_TYPE    =  1;

    private static final double frontView = 45  / 360;
    private static final double sideView  = 135 / 360;
    private static final double backView  = 1;

    private static final int FRONT_VIEW_INDEX = 0;
    private static final int RIGHT_VIEW_INDEX = 1;
    private static final int LEFT_VIEW_INDEX  = 2;
    private static final int BACK_VIEW_INDEX  = 3;

    public static final int MOVE_INDEX  = 0;
    public static final int LEFT_INDEX  = 1;
    public static final int RIGHT_INDEX = 2;
    public static final int FIRE_INDEX  = 3;

    List<Input> inputList;
    double[] netInput;

    public FFANNPhenotypeController3(IPhenotype<DoubleArrayGenotype> ffann) {
        super(ffann);
        this.inputList = new ArrayList<>(4);
        this.netInput = new double[5];
    }

    @Override
    public List<Input> getInput() {
        SpriteManager sm = world.getSpriteManager();

        Ship ship = sm.getShip();

        netInput[SPEED_INDEX] = ship.getVelocity().norm();

        SpriteDistanceAngleMapper mapper = new SpriteDistanceAngleMapper(ship);

        List<SpriteDistanceAngle> stars = sm.getStars().stream().map(mapper::apply).collect(Collectors.toList());
        List<SpriteDistanceAngle> asteroids = sm.getAsteroids().stream().map(mapper::apply).collect(Collectors.toList());

        Map<Integer, List<SpriteDistanceAngle>> starsFields = separateInSpaces(stars);
        Map<Integer, List<SpriteDistanceAngle>> asteroidsFields = separateInSpaces(asteroids);

        putInput(FRONT_TYPE, starsFields.get(FRONT_VIEW_INDEX), asteroidsFields.get(FRONT_VIEW_INDEX));
        putInput(RIGHT_TYPE, starsFields.get(RIGHT_VIEW_INDEX), asteroidsFields.get(RIGHT_VIEW_INDEX));
        putInput(LEFT_TYPE , starsFields.get(LEFT_VIEW_INDEX) , asteroidsFields.get(LEFT_VIEW_INDEX ));
        putInput(BACK_TYPE , starsFields.get(BACK_VIEW_INDEX) , asteroidsFields.get(BACK_VIEW_INDEX ));

        double[] output = phenotype.work(netInput);

        inputList.clear();

        //Set controller inputs
        if(output[MOVE_INDEX] >= 0.5) {
            inputList.add(Input.MOVE);
        }
        if(output[LEFT_INDEX] >= 0.5) {
            inputList.add(Input.LEFT);
        }
        if(output[RIGHT_INDEX] >= 0.5) {
            inputList.add(Input.RIGHT);
        }
        if(output[FIRE_INDEX] >= 0.5) {
            inputList.add(Input.FIRE);
        }

        return inputList;
    }

    private void putInput(int type, List<SpriteDistanceAngle> stars, List<SpriteDistanceAngle> asteroids) {
        Optional<SpriteDistanceAngle> star = stars.stream().min((s1,s2) -> (int)Math.signum(s1.distance - s2.distance));
        Optional<SpriteDistanceAngle> asteroid = asteroids.stream().min((s1,s2) -> (int)Math.signum(s1.distance - s2.distance));

        netInput[type] = 0;
        if(star.isPresent() && asteroid.isPresent()) {
            if(star.get().distance < asteroid.get().distance) {
                netInput[type] = START_TYPE;
            } else {
                netInput[type] = ASTEROID_TYPE;
            }
        } else if(star.isPresent()) {
            netInput[type] = START_TYPE;
        } else if(asteroid.isPresent()) {
            netInput[type] = ASTEROID_TYPE;
        } else {
            netInput[type] = 0;
        }

    }

    private Map<Integer, List<SpriteDistanceAngle>> separateInSpaces(List<SpriteDistanceAngle> spriteDistanceAngles) {
        Map<Integer, List<SpriteDistanceAngle>> sprites = new HashMap<>(4);
        sprites.put(FRONT_VIEW_INDEX, new ArrayList<>());
        sprites.put(RIGHT_VIEW_INDEX, new ArrayList<>());
        sprites.put(LEFT_VIEW_INDEX , new ArrayList<>());
        sprites.put(BACK_VIEW_INDEX , new ArrayList<>());

        spriteDistanceAngles.forEach(s -> {
            double angle = s.angle;

            if(angle < frontView && angle > -frontView) { // angle < 30° && angle > -30° //Front
                sprites.get(FRONT_VIEW_INDEX).add(s);
            } else if(angle > frontView && angle < sideView) { // angle > 30° && angle < 135° //Right
                sprites.get(RIGHT_VIEW_INDEX).add(s);
            } else if(angle < -frontView && angle > -sideView) { // angle < -30° && angle > -135° //Left
                sprites.get(LEFT_VIEW_INDEX).add(s);
            } else { //angle < 180° && angle > -180° //Back
                sprites.get(BACK_VIEW_INDEX).add(s);
            }
        });

        return sprites;
    }

    private static double calculateViewAngle(Ship ship, Sprite sprite) {
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

    private static class SpriteDistanceAngleMapper implements Function<Sprite, SpriteDistanceAngle> {
        private Ship ship;

        public SpriteDistanceAngleMapper(Ship ship) {
            this.ship = ship;
        }

        @Override
        public SpriteDistanceAngle apply(Sprite sprite) {
            IVector shipCenter = ship.getCenter();
            IVector spriteCenter = sprite.getCenter();

            double angle = calculateViewAngle(ship, sprite);

            return new SpriteDistanceAngle(sprite, shipCenter.nSub(spriteCenter).norm(), angle);
        }
    }

    private static class SpriteDistanceAngle {

        public static final Comparator<SpriteDistanceAngle> BY_DISTANCE = (s1, s2) -> (int) Math.signum(s1.distance - s2.distance);

        Sprite sprite;
        double distance;
        double angle;

        public SpriteDistanceAngle(Sprite sprite, double distance, double angle) {
            this.sprite = sprite;
            this.distance = distance;
            this.angle = angle;
        }
    }

}
