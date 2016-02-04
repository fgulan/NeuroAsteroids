package hr.fer.zemris.sm.game.animations;

import javafx.animation.Animation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import static hr.fer.zemris.sm.game.Constants.*;

/**
 * Animations class (static) contains all animations relevant to NeuroAsteroids game. It contains explosion animation,
 * asteroid rotation animation and bonus star rotation animation. For animation to work it needs an ImageView where to
 * play it, Duration, width and height of animation, and number of cycles (Animation.INDEFINITE - indefinite animation)
 */
public class Animations {
    /**
     * Explosion sprite sheet.
     */
    private static final Image explosionImage = new Image(EXPLOSION_ANIMATION_PATH);
    /**
     * Asteroid rotation sprite sheet.
     */
    private static final Image asteroidImage = new Image(ASTEROID_ANIMATION_PATH);
    /**
     * Bonus star rotation sprite sheet.
     */
    private static final Image starImage = new Image(STAR_ANIMATION_PATH);

    /**
     * Asteroid explosion animation.
     * @param imageView ImageView where to play it.
     * @param duration Duration of animation.
     * @param width Width of an image.
     * @param height Height of an image.
     * @param cycles Number of animation cycles. (Animation.INDEFINITE - indefinite animation)
     * @return Asteroid explosion animation.
     */
    public static Animation explosionAnimation(ImageView imageView, Duration duration, int width, int height, int cycles) {
        final Animation animation = new SpriteAnimator(imageView, explosionImage, duration, 64, 0, width, height);
        animation.setCycleCount(cycles);
        return animation;
    }

    /**
     * Asteroid rotation animation.
     * @param imageView ImageView where to play it.
     * @param duration Duration of animation.
     * @param width Width of an image.
     * @param height Height of an image.
     * @param cycles Number of animation cycles. (Animation.INDEFINITE - indefinite animation)
     * @return Asteroid rotation animation.
     */
    public static Animation asteroidRotation(ImageView imageView, Duration duration, int width, int height, int cycles) {
        final Animation animation = new SpriteAnimator(imageView, asteroidImage, duration, 32, 8, width, height);
        animation.setCycleCount(cycles);
        return animation;
    }

    /**
     * Star rotation animation.
     * @param imageView ImageView where to play it.
     * @param duration Duration of animation.
     * @param width Width of an image.
     * @param height Height of an imagg.
     * @param cycles Number of animation cycles. (Animation.INDEFINITE - indefinite animation)
     * @return Star rotation animation.
     */
    public static Animation starRotation(ImageView imageView, Duration duration, int width, int height, int cycles) {
        final Animation animation = new SpriteAnimator(imageView, starImage, duration, 60, 0, width, height);
        animation.setCycleCount(cycles);
        return animation;
    }
}
