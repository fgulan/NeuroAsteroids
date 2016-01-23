package hr.fer.zemris.sm.game.models;

import hr.fer.zemris.sm.game.physics.IVector;
import hr.fer.zemris.sm.game.physics.Vector;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 * Sprite represents a game object in world. It has its velocity vector (2D), collision bounds and real bounds.
 */
public abstract class Sprite {

    /**
     * Sprite velocity.
     */
    protected IVector velocity;
    /**
     * Sprite collision bounds.
     */
    protected Circle collisionBounds;
    /**
     * Sprite real bounds.
     */
    protected Shape bounds;

    /**
     * Sprite constructor.
     */
    public Sprite() {
        super();
    }

    /**
     * Creates sprite with given velocity.
     * @param velocity Sprite velocity.
     */
    public Sprite(IVector velocity) {
        super();
        this.velocity = velocity;
    }

    /**
     * Creates sprite with given velocity and real bounds.
     * @param velocity Sprite velocity.
     * @param bounds Sprite real bounds.
     */
    public Sprite(IVector velocity, Shape bounds) {
        super();
        this.velocity = velocity;
        this.bounds = bounds;
    }

    /**
     * Abstract method for updating position of sprite.
     */
    public abstract void update();

    /**
     * Checks if current sprite intersects with given spire.
     * @param other Other sprite.
     * @return true if current sprites is in collision with given sprite, false otherwise.
     */
    public boolean intersects(Sprite other) {
        if (this == other) {
            return false;
        }
        if (collisionBoundsIntersects(other.collisionBounds)) {
            Shape intersection = Shape.intersect(this.bounds, other.bounds);
            if (intersection.getBoundsInParent().getHeight() <= 0 || intersection.getBoundsInParent().getWidth() <= 0) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if current sprite intersects with given sprite collision bounds.
     * @param other Other sprite collision bounds.
     * @return true if current sprite intersects with given sprite collision bounds, false otherwise.
     */
    private boolean collisionBoundsIntersects(Circle other) {
        double dx = collisionBounds.getCenterX() - other.getCenterX();
        double dy = collisionBounds.getCenterY() - other.getCenterY();
        double minimalDistance = collisionBounds.getRadius() + other.getRadius();
        boolean iz = Math.sqrt(dx * dx + dy * dy) <= minimalDistance;
        return iz;
    }

    /**
     * Sets x position of sprite.
     * @param x New x position of sprite.
     */
    public void translateX(double x) {
        collisionBounds.setCenterX(x);
        bounds.setTranslateX(x);
    }

    /**
     * Sets y position of sprite.
     * @param y New y position of sprite.
     */
    public void translateY(double y) {
        collisionBounds.setCenterY(y);
        bounds.setTranslateY(y);
    }

    /**
     * Gets current sprite velocity vector (2D).
     * @return Current sprite velocity vector (2D).
     */
    public IVector getVelocity() {
        return velocity;
    }

    /**
     * Sets a given velocity to current sprite.
     * @param velocity New velocity vector (2D).
     */
    public void setVelocity(IVector velocity) {
        this.velocity = velocity;
    }

    /**
     * Gets center position of current sprite (2D Vector).
     * @return Center position of current sprite (2D Vector).
     */
    public IVector getCenter() {
        return new Vector(collisionBounds.getCenterX(), collisionBounds.getCenterY());
    }

    /**
     * Gets current sprite collision bounds.
     * @return Current sprite collision bounds.
     */
    public Circle getCollisionBounds() {
        return collisionBounds;
    }

    /**
     * Sets given collision bounds to current sprite.
     * @param collisionBounds New collision bounds.
     */
    public void setCollisionBounds(Circle collisionBounds) {
        this.collisionBounds = collisionBounds;
    }

    /**
     * Gets current sprite real bounds.
     * @return Current sprite real bounds.
     */
    public Shape getBounds() {
        return bounds;
    }

    /**
     * Sets given bounds to current sprite.
     * @param bounds New real bounds.
     */
    public void setBounds(Shape bounds) {
        this.bounds = bounds;
    }
}
