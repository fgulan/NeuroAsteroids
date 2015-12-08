package hr.fer.zemris.sm.game.models;

import hr.fer.zemris.sm.game.physics.IVector;
import hr.fer.zemris.sm.game.physics.Vector;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public abstract class Sprite {
    
    protected IVector velocity;
    protected Circle collisionBounds;
    protected Shape bounds;
    
    public Sprite() {
        super();
    }
    
    public Sprite(IVector velocity) {
        super();
        this.velocity = velocity;
    }

    public Sprite(IVector velocity, Shape bounds) {
        super();
        this.velocity = velocity;
        this.bounds = bounds;
    }
    
    public abstract void update();
    
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
    
    public boolean collisionBoundsIntersects(Circle other) {
        double dx = collisionBounds.getCenterX() - other.getCenterX();
        double dy = collisionBounds.getCenterY() - other.getCenterY();
        double minimalDistance = collisionBounds.getRadius() + other.getRadius();
        boolean iz = Math.sqrt(dx * dx + dy * dy) <= minimalDistance;
        return iz;
    }
    
    public void translateX(double x) {
        collisionBounds.setCenterX(x);
        bounds.setTranslateX(x);
    }

    public void translateY(double y) {
        collisionBounds.setCenterY(y);
        bounds.setTranslateY(y);
    }

    public IVector getVelocity() {
        return velocity;
    }

    public void setVelocity(IVector velocity) {
        this.velocity = velocity;
    }

    public IVector getCenter() {
        return new Vector(collisionBounds.getCenterX(), collisionBounds.getCenterY());
    }
    
    public Circle getCollisionBounds() {
        return collisionBounds;
    }

    public void setCollisionBounds(Circle collisionBounds) {
        this.collisionBounds = collisionBounds;
    }

    public Shape getBounds() {
        return bounds;
    }

    public void setBounds(Shape bounds) {
        this.bounds = bounds;
    }
}
