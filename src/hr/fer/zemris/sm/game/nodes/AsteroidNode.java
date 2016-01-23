package hr.fer.zemris.sm.game.nodes;

import hr.fer.zemris.sm.game.animations.Animations;
import hr.fer.zemris.sm.game.models.Asteroid;
import javafx.animation.Animation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * AsteroidNode class represent NeuroAsteroids asteroid game node.
 */
public class AsteroidNode extends GameNode {

    /**
     * Asteroid rotation animation.
     */
    private Animation animation;

    /**
     * Asteroid game node constructor.
     * @param sprite Asteroid sprite.
     */
    public AsteroidNode(Asteroid sprite) {
        super();
        this.sprite = sprite;
        ImageView asteroid = new ImageView();
        asteroid.setFitWidth(80);
        asteroid.setPreserveRatio(true);
        asteroid.setViewport(new Rectangle2D(0, 0, 128, 128));
        this.node = asteroid;
        node.setLayoutX(-40);
        node.setLayoutY(-40);

        animation = Animations.asteroidRotation(asteroid, Duration.millis(800), 128, 128, Animation.INDEFINITE);
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
