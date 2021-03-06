package hr.fer.zemris.sm.game.models;

import hr.fer.zemris.sm.game.physics.IVector;
import hr.fer.zemris.sm.game.physics.Vector;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

/**
 * Represents missile sprite. Its collision bound is Circle and real bound is Polygon.
 */
public class Missile extends Sprite {

    private float angle;

    /**
     * Creates missile with given speed and direction.
     * @param angle Missile direction.
     * @param speed Missile speed.
     */
    public Missile(float angle, double speed) {
        super();
        collisionBounds = new Circle(0, 0, 17);
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[] { 3.0, 0.0, 
                                                  8.0, 0.0, 
                                                  8.0, 30.0, 
                                                  3.0, 30.0 });
        bounds = polygon;
        bounds.setRotate(angle);
        bounds.setLayoutX(-5);
        bounds.setLayoutY(-15);
        collisionBounds.setRotate(angle);
        velocity = getSpeed(angle).scalarMultiply(speed);
        this.angle = angle;
    }

    @Override
    public void update() {
        double x = bounds.getTranslateX() + velocity.get(0);
        double y = bounds.getTranslateY() + velocity.get(1);
        translateX(x);
        translateY(y);
    }

    /**
     * Gets current missile angle.
     * @return Current missile angle.
     */
    public float getAngle() {
        return angle;
    }

    /**
     * Sets a given angle to current missile.
     * @param angle New missile angle.
     */
    public void setAngle(float angle) {
        this.angle = angle;
        bounds.setRotate(angle);
        collisionBounds.setRotate(angle);
        velocity = getSpeed(angle);
    }
}
