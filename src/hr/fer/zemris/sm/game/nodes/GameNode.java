package hr.fer.zemris.sm.game.nodes;

import hr.fer.zemris.sm.game.models.Sprite;
import javafx.scene.Node;

/**
 * GameNode class is wrapper class for NeuroAsteroids game sprite. It connects given sprite with its graphic representation.
 */
public abstract class GameNode {

    /**
     * Game sprite
     */
    protected Sprite sprite;
    /**
     * Graphic representation
     */
    protected Node node;

    /**
     * Update location of current node.
     */
    public abstract void update();

    /**
     * Sets x position of current node.
     * @param x New x position.
     */
    public abstract void translateX(double x);

    /**
     * Sets y position of current node.
     * @param y New y position.
     */
    public abstract void translateY(double y);

    /**
     * Returns sprite of current game node.
     * @return Current game node sprite.
     */
    public Sprite getSprite() {
        return sprite;
    }

    /**
     * Sets given sprite to current game node.
     * @param sprite New sprite.
     */
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    /**
     * Gets current graphic node.
     * @return Graphic node
     */
    public Node getNode() {
        return node;
    }

    /**
     * Sets given graphic node.
     * @param node Graphic Node.
     */
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * Simulate explosion of current game node.
     * @param handler ExplosionHandler listener.
     */
    public abstract void explode(ExplosionHandler handler);
}
