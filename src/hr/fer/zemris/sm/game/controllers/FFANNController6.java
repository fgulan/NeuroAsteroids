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
import java.util.Optional;
import java.util.function.Function;

/**
 * Network sees same info as FFANNController5 with slightly different inputs.
 *
 * Created by Fredi Šarić on 22.12.15.
 */
public class FFANNController6 extends AbstractPhenotypeController {

    private double[] netInput;
    private List<Input> inputList;

    private int moveCounter;
    private int leftCounter;
    private int rightCounter;
    private int fireCounter;

    public FFANNController6(IPhenotype ffann) {
        super(ffann);
        this.inputList = new ArrayList<>(4);
        netInput = new double[4];
    }

    @Override
    public List<Input> getInput() {
        SpriteManager sm = world.getSpriteManager();

        Ship ship = sm.getShip();

        netInput[0] = ship.getVelocity().norm();

        SpriteDistanceAngleMapper mapper = new SpriteDistanceAngleMapper(ship);
        Optional<SpriteDistanceAngle> star = sm.getStars().stream().map(mapper::apply).min((s1, s2) -> (int)Math.signum(s1.distance - s2.distance));
        Optional<SpriteDistanceAngle> aster = sm.getAsteroids().stream().map(mapper::apply).min((s1,s2) -> (int)Math.signum(s1.distance - s2.distance));

        if(star.isPresent()) {  //Ako je 1 -1 odi desno, ako je -1 1 odi ljevo valda to moze skuzit
            netInput[1] = Math.signum(star.get().angle);
            netInput[2] = -netInput[1];
        } else {
            netInput[1] = 0;
            netInput[2] = 0;
        }

        if(aster.isPresent()) {
            double  angle = aster.get().angle;
            if(angle > 30/(double)360 || angle < -30/(double)360){
                netInput[3] = 0;
            } else if(angle > 30/(double)360){
                netInput[3] =  1; //From the right
            } else {
                netInput[3] = -1; //From the left
            }
        } else {
            netInput[3] = 0;
        }

        double[] output = phenotype.work(netInput);

        inputList.clear();
        if(output[0] > 0.5) {
            inputList.add(Input.MOVE);
            moveCounter++;
        }
        if(output[1] > 0.5) {
            inputList.add(Input.LEFT);
            leftCounter ++;
        }
        if(output[2] > 0.5) {
            inputList.add(Input.RIGHT);
            rightCounter++;
        }
        if(output[3] > 0.5) {
            inputList.add(Input.FIRE);
            fireCounter++;
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

    public int getFireCounter() {
        return fireCounter;
    }

    public int getRightCounter() {
        return rightCounter;
    }

    public int getLeftCounter() {
        return leftCounter;
    }

    public int getMoveCounter() {
        return moveCounter;
    }
}