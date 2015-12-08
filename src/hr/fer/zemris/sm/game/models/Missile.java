package hr.fer.zemris.sm.game.models;

import hr.fer.zemris.sm.game.physics.IVector;
import hr.fer.zemris.sm.game.physics.Vector;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class Missile extends Sprite {

    private float angle;
    
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
    
    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        bounds.setRotate(angle);
        collisionBounds.setRotate(angle);
        velocity = getSpeed(angle);
    }
    
    private IVector getSpeed(double angle) {
        double ang = Math.abs(angle);
        double radians;
        double xV = 0;
        double yV = 0;
        if (ang >= 0 && ang <= 90) {
            radians = Math.toRadians(angle);
            xV = Math.sin(radians);
            yV = -Math.cos(radians);
        } else if (ang > 90 && ang <= 180) {
            radians = Math.toRadians(angle - 90);
            xV = Math.cos(radians);
            yV = Math.sin(radians);
        } else if (ang > 180 && ang <= 270) {
            radians = Math.toRadians(angle - 180);
            xV = -Math.sin(radians);
            yV = Math.cos(radians);
        } else if (ang > 270 && ang <= 360) {
            radians = Math.toRadians(angle - 270);
            xV = -Math.cos(radians);
            yV = -Math.sin(radians);
        }
        return new Vector(xV, yV);
    }
}
