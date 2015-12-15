package hr.fer.zemris.sm.game.nodes;

import hr.fer.zemris.sm.game.animations.Animations;
import hr.fer.zemris.sm.game.models.Star;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class StarNode extends GameNode {

    private Animation animation;

    public StarNode(Star sprite) {
        super();
        this.sprite = sprite;
        this.node = new Circle(60);
        ((Circle) this.node).setFill(Color.YELLOW);
        node.setTranslateX(sprite.getBounds().getTranslateX());
        node.setTranslateY(sprite.getBounds().getTranslateY());

        ImageView star = new ImageView();
        star.setFitWidth(60);
        star.setPreserveRatio(true);
        star.setViewport(new Rectangle2D(0, 0, 128, 128));

        this.node = star;
        this.node.setLayoutX(-30);
        this.node.setLayoutY(-30);

        animation = Animations.starRotation(star, Duration.millis(800), 128, 128, Animation.INDEFINITE);
        animation.play();

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
