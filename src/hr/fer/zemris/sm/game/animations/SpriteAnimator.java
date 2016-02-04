package hr.fer.zemris.sm.game.animations;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * SpriteAnimator is simple sprite sheet player using linear interpolator. It can use sprite sheet in matrix or array
 * form. For animation to work it needs an ImageView where to play it, Duration, width and height of animation, and
 * number of cycles (Animation.INDEFINITE - indefinite animation)
 */
public class SpriteAnimator extends Transition {

    private final ImageView imageView;
    private final int rows;
    private final int count;
    private final int width;
    private final int height;
    private int lastIndex;

    /**
     * Creates animation on given image view with given sprite sheet.
     * @param imageView ImageView where to play it.
     * @param spriteSheet Sprite sheet image.
     * @param duration Animation duration.
     * @param count Number of images on sprite sheet.
     * @param rows Number of rows on sprite sheet (0 if it is an array)
     * @param width Width of each image on sprite sheet.
     * @param height Height of each image on sprite sheet.
     */
    public SpriteAnimator(ImageView imageView, Image spriteSheet, Duration duration, int count, int rows, int width, int height) {
        super();
        this.imageView = imageView;
        this.imageView.setImage(spriteSheet);
        this.rows = rows;
        this.count = count;
        this.width = width;
        this.height = height;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    protected void interpolate(double k) {
        final int index = Math.min((int) Math.floor(k * count), count - 1);
        if (index != lastIndex) {
            int x;
            int y = 0;
            if (rows == 0) {
                x = (index % count) * width;
            } else {
                x = (index % rows) * width;
                y = (index / rows) * height;
            }
            imageView.setViewport(new Rectangle2D(x, y, width, height));
            lastIndex = index;
        }
    }
}
