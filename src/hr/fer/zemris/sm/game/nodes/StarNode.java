package hr.fer.zemris.sm.game.nodes;

import hr.fer.zemris.sm.game.models.Star;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class StarNode extends GameNode {
    
    public StarNode(Star sprite) {
        super();
        this.sprite = sprite;
        this.node = new Circle(15);
        ((Circle) this.node).setFill(Color.YELLOW);
        node.setTranslateX(sprite.getBounds().getTranslateX());
        node.setTranslateY(sprite.getBounds().getTranslateY());
    }


    @Override
    public void update() {
        node.setTranslateX(sprite.getBounds().getTranslateX());
        node.setTranslateY(sprite.getBounds().getTranslateY());
    }

    @Override
    public void translateX(double x) {
        sprite.translateX(x);
        node.setTranslateX(x);
    }

    @Override
    public void translateY(double y) {
        sprite.translateY(y);
        node.setTranslateY(y);
    }
    
    @Override
    public void explode(ExplosionHandler handler) {
        handler.handle(this);
    }

}
