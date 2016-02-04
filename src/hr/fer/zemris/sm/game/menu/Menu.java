package hr.fer.zemris.sm.game.menu;

import hr.fer.zemris.sm.Disconnectable;
import javafx.scene.layout.BorderPane;

/**
 * Abstract class that every menu should implement.
 * All menus are disconnectable from the game.
 * Every menu should reload before it is accessed.
 *
 * Created by doctor on 02.12.15..
 */
public abstract class Menu extends BorderPane implements Disconnectable{

    private Game parent;

    public Menu(Game parent) {
        this.parent = parent;
    }

    public Game getGameParent() {
        return parent;
    }

    @Override
    public void disconnect() {
        parent = null;
    }

    public abstract void reload();
}
