package hr.fer.zemris.sm.game.models;

import hr.fer.zemris.sm.game.GameConfig;
import hr.fer.zemris.sm.game.physics.IVector;
import hr.fer.zemris.sm.game.physics.Vector;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import static hr.fer.zemris.sm.game.Constants.*;

public class Ship extends Sprite {

    public enum Direction {
        CLOCKWISE, COUNTER_CLOCKWISE
    }

    private final static int TWO_PI_DEGREES = 360;
    private final static int NUM_DIRECTIONS = 240;
    private final static float UNIT_ANGLE_PER_FRAME = ((float) TWO_PI_DEGREES / NUM_DIRECTIONS);

    private float shipSpeed = 0;
    
    private float currentAngle = 0;
    private boolean move = false;

    private final double acceleration = GameConfig.getInstance().getAcceleration();
    private final double deceleration = GameConfig.getInstance().getDeceleration();

    public Ship() {
        super();
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(40.0, 5.0, 0.0, 60.0, 80.0, 60.0);
        this.bounds = polygon;
        this.collisionBounds = new Circle(0, 0, 50);
        velocity = new Vector(0, 0);
        bounds.setLayoutX(-40);
        bounds.setLayoutY(-30);
    }

    @Override
    public void update() {
        if (move) {
            IVector velocityDirection = getSpeed(currentAngle);
            if (shipSpeed < 0) {
                shipSpeed = 0;
            }
            if (shipSpeed < MAX_SPEED) {
                shipSpeed += acceleration;
            }
            velocity = velocityDirection.scalarMultiply(shipSpeed);
            move = false;
        } else {
            if (shipSpeed > 0) {
                shipSpeed -= deceleration;
                IVector velocityDirection = getSpeed(currentAngle);
                velocity = velocityDirection.scalarMultiply(shipSpeed);
            } else {
                velocity = new Vector(0, 0);
                shipSpeed = 0;
            }
        }
        double x = bounds.getTranslateX() + velocity.get(0);
        double y = bounds.getTranslateY() + velocity.get(1);
        translateX(x);
        translateY(y);
    }

    public void rotate(Direction direction) {
        if (direction == Direction.CLOCKWISE) {
            currentAngle += UNIT_ANGLE_PER_FRAME;
        } else if (direction == Direction.COUNTER_CLOCKWISE) {
            currentAngle -= UNIT_ANGLE_PER_FRAME;
        }
        currentAngle %= 360;
        bounds.setRotate(currentAngle);
    }

    public void move() {
        this.move = true;
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

    public float getCurrentAngle() {
        return currentAngle;
    }
}
