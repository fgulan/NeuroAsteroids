package hr.fer.zemris.sm.game.nodes;

import hr.fer.zemris.sm.game.animations.Animations;
import hr.fer.zemris.sm.game.models.Ship;
import javafx.animation.Animation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import static hr.fer.zemris.sm.game.Constants.SHIP_IMAGE_PATH;

/**
 * ShipNode class represent NeuroAsteroids ship game node.
 */
public class ShipNode extends GameNode {

    /**
     * ShipNode constructor.
     * @param sprite Ship sprite.
     */
    public ShipNode(Ship sprite) {
        super();
        this.sprite = sprite;

        ImageView ship = new ImageView(SHIP_IMAGE_PATH);
        ship.setFitWidth(80);
        ship.setPreserveRatio(true);
        this.node = ship;

        node.setLayoutX(-40);
        node.setLayoutY(-35);
    }

    @Override
    public void update() {
        node.setTranslateX(sprite.getBounds().getTranslateX());
        node.setTranslateY(sprite.getBounds().getTranslateY());
        node.setRotate(((Ship) sprite).getCurrentAngle());
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
        ImageView ship = (ImageView) node;
        node.setLayoutX(-96);
        node.setLayoutY(-96);
        ship.setFitWidth(196);
        ship.setPreserveRatio(true);
        Animation animation = Animations.explosionAnimation(ship, Duration.seconds(2), 192, 192, 1);
        animation.setOnFinished(event -> handler.handle(ShipNode.this));
        animation.play();
    }
}
