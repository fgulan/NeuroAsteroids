package hr.fer.zemris.game.nodes;

import hr.fer.zemris.game.models.Sprite;
import javafx.scene.Node;

public abstract class GameNode {
    
    protected Sprite sprite;
    protected Node node;
    
    public abstract void update();

    public abstract void translateX(double x);

    public abstract void translateY(double y);
    
    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
    
    public abstract void explode(ExplosionHandler handler);
}
