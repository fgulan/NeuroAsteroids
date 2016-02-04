package hr.fer.zemris.sm.game.controllers;

import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.game.models.Ship;
import hr.fer.zemris.sm.game.models.Sprite;
import hr.fer.zemris.sm.game.physics.IVector;
import hr.fer.zemris.sm.game.world.GameWorld;

/**
 *
 * Created by Fredi Šarić on 11.01.16.
 */
public abstract class AbstractPhenotypeController implements IController {

    public static final long serialVersionUID = 6027582259431556259L;

    GameWorld world;

    IPhenotype phenotype;

    public AbstractPhenotypeController(IPhenotype phenotype) {
        this.phenotype = phenotype;
    }

    @Override
    public void setWorld(GameWorld world) {
        this.world = world;
    }

    @Override
    public void disconnect() {
        world = null;
    }

    protected double calculateAngle(Ship ship, Sprite sprite) {

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
        return - alpha; //Return in degrees
    }

    protected static double calcDistance(Ship ship, Sprite sprite) {
        return ship.getCenter().nSub(sprite.getCenter()).norm();
    }
}
