package hr.fer.zemris.sm.game.controllers;

import hr.fer.zemris.sm.evolution.representation.FFANN.DoubleArrayGenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.game.managers.SpriteManager;
import hr.fer.zemris.sm.game.models.Ship;
import hr.fer.zemris.sm.game.models.Sprite;
import hr.fer.zemris.sm.game.physics.IVector;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Fredi Šarić on 21.12.15.
 */
public class FFANNPhenotypeController2 extends FFANNPhenotypeController {

    /**
     * Inputs:
     * Inherited
     * First:  speed of the network
     * <p>
     * Second: angle at which network sees star
     * Third:  is star distance
     *
     *
     *
     * <p>
     * Fourth: angle at which network sees FRONT asteroid
     * Fifth:  distance of FRONT asteroid
     * <p>
     * Sixth:  angle at which network sees LEFT asteroid
     * Seventh distance of LEFT asteroid
     * <p>
     * Eight:  angle at which network sees RIGHT asteroid
     * Ninth:  distance of RIGHT asteroid
     * <p>
     * TENTH:  distance of BEHIND asteroid
     */

    public static final int FRONT_ASTEROID_ANGLE    = 3;
    public static final int FRONT_ASTEROID_DISTANCE = 4;
    public static final int LEFT_ASTEROID_ANGLE     = 5;
    public static final int LEFT_ASTEROID_DISTANCE  = 6;
    public static final int RIGHT_ASTEROID_ANGLE    = 7;
    public static final int RIGHT_ASTEROID_DISTANCE = 8;
    public static final int BACK_ASTEROID_DISTANCE  = 9;

    private static final double frontView = 45  / 360;
    private static final double sideView  = 135 / 360;
    private static final double backView  = 1;

    private static final int FRONT_VIEW_INDEX = 0;
    private static final int RIGHT_VIEW_INDEX = 1;
    private static final int LEFT_VIEW_INDEX  = 2;
    private static final int BACK_VIEW_INDEX  = 3;

    public FFANNPhenotypeController2(IPhenotype<DoubleArrayGenotype> ffann) {
        super(ffann);
        this.inputList = new ArrayList<>(4);
        this.netInput = new double[10];
    }

    @Override
    protected void putAsteroidInfo(SpriteManager sm, Ship ship) {

        SpriteDistanceAngleMapper mapper = new SpriteDistanceAngleMapper(ship);

        List<SpriteDistanceAngle> spriteDistanceAngles  = sm.getAsteroids()
                                                              .stream()
                                                              .map(mapper::apply)
                                                              .collect(Collectors.toList());


        Map<Integer, List<SpriteDistanceAngle>> asteroids = spearateInSpaces(spriteDistanceAngles);

        //Add front asteroid info
        putInfo(asteroids.get(FRONT_VIEW_INDEX), FRONT_ASTEROID_ANGLE, FRONT_ASTEROID_DISTANCE);

        //Add right asteroid info
        putInfo(asteroids.get(RIGHT_VIEW_INDEX), RIGHT_ASTEROID_ANGLE, RIGHT_ASTEROID_DISTANCE);

        //Add left asteroid info
        putInfo(asteroids.get(LEFT_VIEW_INDEX), LEFT_ASTEROID_ANGLE,LEFT_ASTEROID_DISTANCE);

        //Add back asteroid info
        putInfo(asteroids.get(BACK_VIEW_INDEX), -1, LEFT_ASTEROID_DISTANCE);
    }

    private void putInfo(List<SpriteDistanceAngle> spriteDistanceAngles, int angle, int distance) {
        Optional<SpriteDistanceAngle> side = spriteDistanceAngles.stream().min(SpriteDistanceAngle.BY_DISTANCE);
        if(side.isPresent()) {
            if(angle != -1) {
                netInput[angle] = side.get().angle;
            }
            netInput[distance]  = side.get().distance;
        } else {
            if(angle != -1) {
                netInput[angle] = 0;
            }
            netInput[distance] = 0;
        }
    }

    private Map<Integer, List<SpriteDistanceAngle>> spearateInSpaces(List<SpriteDistanceAngle> spriteDistanceAngles) {
        Map<Integer, List<SpriteDistanceAngle>> asteroids = new HashMap<>(4);
        asteroids.put(FRONT_VIEW_INDEX, new ArrayList<>());
        asteroids.put(RIGHT_VIEW_INDEX, new ArrayList<>());
        asteroids.put(LEFT_VIEW_INDEX , new ArrayList<>());
        asteroids.put(BACK_VIEW_INDEX , new ArrayList<>());

        spriteDistanceAngles.forEach(s -> {
            double angle = s.angle;

            if(angle < frontView && angle > -frontView) { // angle < 30° && angle > -30° //Front
                asteroids.get(FRONT_VIEW_INDEX).add(s);
            } else if(angle > frontView && angle < sideView) { // angle > 30° && angle < 135° //Right
                asteroids.get(RIGHT_VIEW_INDEX).add(s);
            } else if(angle < -frontView && angle > -sideView) { // angle < -30° && angle > -135° //Left
                asteroids.get(LEFT_VIEW_INDEX).add(s);
            } else { //angle < 180° && angle > -180° //Back
                asteroids.get(BACK_VIEW_INDEX).add(s);
            }
        });


        return asteroids;
    }

    @Override
    protected void putEmptyAsteroidInfo() {
        netInput[FRONT_ASTEROID_ANGLE] = 0;
        netInput[FRONT_ASTEROID_DISTANCE] = 0;

        netInput[RIGHT_ASTEROID_ANGLE] = 0;
        netInput[RIGHT_ASTEROID_DISTANCE] = 0;

        netInput[LEFT_ASTEROID_ANGLE] = 0;
        netInput[LEFT_ASTEROID_DISTANCE] = 0;

        netInput[BACK_ASTEROID_DISTANCE] = 0;
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