package hr.fer.zemris.game.models;

import hr.fer.zemris.game.physics.IVector;
import hr.fer.zemris.game.physics.Vector;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class Ship extends Sprite {

    public enum Direction {
        CLOCKWISE, COUNTER_CLOCKWISE, NEITHER
    }

    private final static int TWO_PI_DEGREES = 360;
    private final static int NUM_DIRECTIONS = 180;
    private final static float UNIT_ANGLE_PER_FRAME = ((float) TWO_PI_DEGREES / NUM_DIRECTIONS);

    private final static float ACCELERATION_STEP = 0.2f;
    private final static float MAX_SPEED = 5.5f;
    private float shipSpeed = 0;
    
    private float currentAngle = 0;
    private boolean move = false;
    private Direction turnDirection = Direction.NEITHER;

    public Ship() {
        super();
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[] { 40.0, 5.0, 0.0, 60.0, 80.0, 60.0 });
        this.bounds = polygon;
        this.collisionBounds = new Circle(0, 0, 50);
        velocity = new Vector(0, 0);
        bounds.setLayoutX(-40);
        bounds.setLayoutY(-30);
    }

    @Override
    public void update() {
        if (turnDirection == Direction.CLOCKWISE) {
            currentAngle += UNIT_ANGLE_PER_FRAME;
            bounds.setRotate(currentAngle);
        } else if (turnDirection == Direction.COUNTER_CLOCKWISE) {
            currentAngle -= UNIT_ANGLE_PER_FRAME;
            bounds.setRotate(currentAngle);
        }
        currentAngle %= 360;
        if (move) {
            IVector velocityDirection = getSpeed(currentAngle);
            if (shipSpeed < 0) {
                shipSpeed = 0;
            }
            if (shipSpeed < MAX_SPEED) {
                shipSpeed += ACCELERATION_STEP;
            }
            velocity = velocityDirection.scalarMultiply(shipSpeed);
        } else {
            if (shipSpeed > 0) {
                shipSpeed -= 0.15*ACCELERATION_STEP;
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
        turnDirection = direction;
    }

    public void move(boolean move) {
        this.move = move;
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
