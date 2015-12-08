package hr.fer.zemris.sm.game.models;

import hr.fer.zemris.sm.game.physics.IVector;
import hr.fer.zemris.sm.game.physics.Vector;
import javafx.scene.shape.Circle;

public class Asteroid extends Sprite {
    
    public Asteroid(IVector velocity) {
        super(velocity);
        collisionBounds = new Circle(0, 0, 29);
        bounds = new Circle(0, 0, 29);
    }

    @Override
    public void update() {
        double x = bounds.getTranslateX() + velocity.get(0);
        double y = bounds.getTranslateY() + velocity.get(1);
        translateX(x);
        translateY(y);
    }
    
    public void bounceOf(Asteroid other) {
        Circle firstBounds = this.collisionBounds;
        Circle secondBounds = other.collisionBounds;
        double dx = secondBounds.getCenterX() - firstBounds.getCenterX();
        double dy = secondBounds.getCenterY() - firstBounds.getCenterY();

        IVector uNormal = new Vector(dx, dy).nNormalize();
        IVector uTangent = new Vector(-uNormal.get(1), uNormal.get(0));

        double v1n = uNormal.scalarProduct(this.velocity);
        double v1t = uTangent.scalarProduct(this.velocity);
        double v2n = uNormal.scalarProduct(other.velocity);
        double v2t = uTangent.scalarProduct(other.velocity);

        IVector newV1nVector = uNormal.nScalarMultiply(v2n);
        IVector newV1tVector = uTangent.nScalarMultiply(v1t);

        IVector newV2nVector = uNormal.nScalarMultiply(v1n);
        IVector newV2tVector = uTangent.nScalarMultiply(v2t);

        this.velocity = newV1nVector.nAdd(newV1tVector);
        other.velocity = newV2nVector.nAdd(newV2tVector);
        
        //METAK METAK METAK
        double distance = Math.sqrt(dx*dx + dy*dy) - firstBounds.getRadius() - secondBounds.getRadius();
        if (distance < 0) {
            IVector normalized = velocity.nNormalize();
            distance = Math.abs(distance);
            double angle = Math.tan(normalized.get(0)/normalized.get(1));
            double x = Math.sin(angle)*distance;
            double y = Math.cos(angle)*distance;
            translateX(bounds.getTranslateX() + x);
            translateY(bounds.getTranslateY() + y);
        }
        
        this.update();
        other.update();
    }
}
