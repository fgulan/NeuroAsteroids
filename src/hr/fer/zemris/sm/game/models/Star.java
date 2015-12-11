package hr.fer.zemris.sm.game.models;

import hr.fer.zemris.sm.game.physics.Vector;
import javafx.scene.shape.Circle;

public class Star extends Sprite {

    public Star() {
        super(new Vector(0, 0));
        this.collisionBounds = new Circle(15);
        this.bounds = new Circle(15);
    }

    @Override
    public void update() {
        double x = bounds.getTranslateX() + velocity.get(0);
        double y = bounds.getTranslateY() + velocity.get(1);
        translateX(x);
        translateY(y);
    }

}
