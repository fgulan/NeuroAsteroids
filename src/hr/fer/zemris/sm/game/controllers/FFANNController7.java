package hr.fer.zemris.sm.game.controllers;

import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.game.managers.SpriteManager;
import hr.fer.zemris.sm.game.models.Ship;
import hr.fer.zemris.sm.game.models.Sprite;
import hr.fer.zemris.sm.game.physics.IVector;
import hr.fer.zemris.sm.game.world.GameWorld;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 *
 * S ovim kontrolerom mreza vidi samo dali je zvjezdica ljevo od nje ili desno.
 * Dali je asteroid u vidnom polju, dali je ljevo, dali je desno (0, -1, 1)
 * Udaljenost od asteroida skalirana na -1 do 1 (min udaljenost je 80, max 600)
 * Dali je puco u zandnjih 10 inputa 1 ako nije 0 ako je
 *
 * Created by Fredi Šarić on 23.12.15.
 */
public class FFANNController7 extends AbstractPhenotypeController {

    private static final int FRAME_COUNT_INTERVAL = 10;

    private static final int MIN_DISTANCE = 80;
    private static final int MAX_DISTANCE = 600;

    private static final double A = 1.0/260;
    private static final double B = -17.0/13;

    private List<Input> inputList;
    private double[] netInput;

    private int fireCounter;

    public FFANNController7(IPhenotype phenotype) {
        super(phenotype);
        this.inputList = new ArrayList<>(4);
        this.netInput = new double[4];
        fireCounter = 10;
    }

    public void setWorld(GameWorld world) {
        this.world = world;
    }

    @Override
    public List<Input> getInput() {
        SpriteManager sm = world.getSpriteManager();
        Ship ship = sm.getShip();
        SpriteDistanceAngleMapper mapper = new SpriteDistanceAngleMapper(ship);

        if(sm.getStars().size() > 0) {
            netInput[0] = Math.signum(sm.getStars().stream().map(mapper::apply).min((SpriteDistanceAngle.BY_DISTANCE)).get().angle);
        } else {
            netInput[0] = 0;
        }

        if(sm.getAsteroids().size() > 0) {
            SpriteDistanceAngle a = sm.getAsteroids().stream().map(mapper::apply).min(SpriteDistanceAngle.BY_DISTANCE).get();
            if(a.angle < 30/(double)360 && a.angle > -30/(double)360) {
                netInput[1] = 0;
            } else {
                netInput[1] = Math.signum(a.angle);
            }

            netInput[2] = a.distance * A + B;

            if(fireCounter <= 0) {
                netInput[3] = 1;
            } else {
                netInput[3] = 0;
            }
        }

        double[] output = phenotype.work(netInput);

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
            fireCounter = 10;
        } else {
            fireCounter--;
        }

        return inputList;
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

}
