package hr.fer.zemris.sm.game.models;

import hr.fer.zemris.sm.game.physics.Vector;
import javafx.scene.shape.Circle;

/**
 * Represents star sprite. Its collision bounds and real bounds are Circle.
 */
public class Star extends Sprite {

    /**
     * Star constructor
     */
    public Star() {
        super(new Vector(0, 0));
        this.collisionBounds = new Circle(20);
        this.bounds = new Circle(20);
    }

    @Override
    public void update() {
        double x = bounds.getTranslateX() + velocity.get(0);
        double y = bounds.getTranslateY() + velocity.get(1);
        translateX(x);
        translateY(y);
    }

}
