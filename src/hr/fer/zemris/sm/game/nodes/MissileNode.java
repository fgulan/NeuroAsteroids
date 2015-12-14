package hr.fer.zemris.sm.game.nodes;

import hr.fer.zemris.sm.game.models.Missile;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static hr.fer.zemris.sm.game.Constants.MISSILE_IMAGE_PATH;

public class MissileNode extends GameNode {

    private static final Image missileImage = new Image(MISSILE_IMAGE_PATH);

    public MissileNode(Missile sprite) {
        super();
        this.sprite = sprite;

        ImageView missile = new ImageView(missileImage);
        missile.setFitHeight(50);
        missile.setPreserveRatio(true);
        missile.setLayoutX(-10);
        missile.setLayoutY(-25);
        this.node = missile;
    }

    @Override
    public void update() {
        node.setTranslateX(sprite.getBounds().getTranslateX());
        node.setTranslateY(sprite.getBounds().getTranslateY());
        node.setRotate(((Missile) sprite).getAngle());
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
