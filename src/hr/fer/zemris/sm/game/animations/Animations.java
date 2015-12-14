package hr.fer.zemris.sm.game.animations;

import javafx.animation.Animation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import static hr.fer.zemris.sm.game.Constants.EXPLOSION_ANIMATION_PATH;
import static hr.fer.zemris.sm.game.Constants.ASTEROID_ANIMATION_PATH;


public class Animations {
    
    private static final Image explosionImage = new Image(EXPLOSION_ANIMATION_PATH);
    private static final Image commetImage = new Image(ASTEROID_ANIMATION_PATH);

    
    public static Animation explosionAnimation(ImageView imageView, Duration duration, int width, int height, int cycles) {
        imageView.setImage(explosionImage);
        final Animation animation = new SpriteAnimator(imageView, duration, 64, 0, width, height);
        animation.setCycleCount(cycles);
        return animation;
    }
    public static Animation commetRotation(ImageView imageView, Duration duration, int width, int height, int cycles) {
        imageView.setImage(commetImage);
        final Animation animation = new SpriteAnimator(imageView, duration, 32, 8, width, height);
        animation.setCycleCount(cycles);
        return animation;
    }
}
