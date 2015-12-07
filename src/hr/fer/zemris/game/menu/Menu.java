package hr.fer.zemris.game.menu;

import javafx.scene.layout.BorderPane;

/**
 * Created by doctor on 02.12.15..
 */
public abstract class Menu extends BorderPane {

    private Game parent;

    public Menu(Game parent) {
        this.parent = parent;
    }

    public Game getGameParent() {
        return parent;
    }

    public void disconnect() {
        parent = null;
    }
}
