package hr.fer.zemris.game.animations;

import javafx.animation.Animation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Animations {
    
    private static final Image explosionImage = new Image("exp2.png");
    private static final Image commetImage = new Image("rocks.png");

    
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
