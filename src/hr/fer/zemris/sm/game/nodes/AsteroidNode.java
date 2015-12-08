package hr.fer.zemris.sm.game.nodes;

import hr.fer.zemris.sm.game.animations.Animations;
import hr.fer.zemris.sm.game.models.Asteroid;
import javafx.animation.Animation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class AsteroidNode extends GameNode {

    private Animation animation;
    
    public AsteroidNode(Asteroid sprite) {
        super();
        this.sprite = sprite;
        ImageView commet = new ImageView();
        commet.setFitWidth(80);
        commet.setPreserveRatio(true);
        commet.setViewport(new Rectangle2D(0, 0, 128, 128));
        this.node = commet;
        node.setLayoutX(-40);
        node.setLayoutY(-40);

        animation = Animations.commetRotation(commet, Duration.millis(800), 128, 128, Animation.INDEFINITE);
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
        animation.stop();
        ImageView commet = (ImageView) node;
        node.setLayoutX(-96);
        node.setLayoutY(-96);
        commet.setFitWidth(196);
        commet.setPreserveRatio(true);
        animation = Animations.explosionAnimation(commet, Duration.seconds(2), 192, 192, 1);
        animation.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handler.handle(AsteroidNode.this);
            }
        });
        animation.play();
    }
}
