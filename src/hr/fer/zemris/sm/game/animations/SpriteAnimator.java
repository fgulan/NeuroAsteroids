package hr.fer.zemris.sm.game.animations;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpriteAnimator extends Transition {

    private final ImageView imageView;
    private final int columns;
    private final int count;
    private final int width;
    private final int height;
    private int lastIndex;

    public SpriteAnimator(ImageView imageView, Duration duration, int count, int columns, int width, int height) {
        super();
        this.imageView = imageView;
        this.columns = columns;
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
            int y;
            if (columns == 0) {
                y = 0;
                x = (index % count) * width;
            } else {
                x = (index % columns) * width;
                y = (index / columns) * height;
            }
            imageView.setViewport(new Rectangle2D(x, y, width, height));
            lastIndex = index;
        }
    }
}
