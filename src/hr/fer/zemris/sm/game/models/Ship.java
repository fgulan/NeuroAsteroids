package hr.fer.zemris.sm.game.models;

import hr.fer.zemris.sm.game.GameConfig;
import hr.fer.zemris.sm.game.physics.IVector;
import hr.fer.zemris.sm.game.physics.Vector;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import static hr.fer.zemris.sm.game.Constants.*;

/**
 * Represents ship sprite. Its collision bound is Circle and real bound is Polygon.
 */
public class Ship extends Sprite {

    /**
     * Ship rotation direction.
     */
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

    /**
     * Ship constructor.
     */
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

    /**
     * Rotates ship sprite in given direction.
     * @param direction Rotation direction.
     */
    public void rotate(Direction direction) {
        if (direction == Direction.CLOCKWISE) {
            currentAngle += UNIT_ANGLE_PER_FRAME;
        } else if (direction == Direction.COUNTER_CLOCKWISE) {
            currentAngle -= UNIT_ANGLE_PER_FRAME;
        }
        currentAngle %= 360;
        bounds.setRotate(currentAngle);
    }

    /**
     * Sets move flag to the ship.
     */
    public void move() {
        this.move = true;
    }

    /**
     * Returns current angle of ship.
     * @return Ship current angle.
     */
    public float getCurrentAngle() {
        return currentAngle;
    }
}
