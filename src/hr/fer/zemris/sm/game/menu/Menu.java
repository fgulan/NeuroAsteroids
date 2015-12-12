package hr.fer.zemris.sm.game.menu;

import hr.fer.zemris.sm.game.controllers.KeyboardController;
import hr.fer.zemris.sm.game.sound.EffectsSoundManager;
import hr.fer.zemris.sm.game.world.GraphicsWorld;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static hr.fer.zemris.sm.game.Constants.GAME_WORLD_STYLE_PATH;

/**
 *
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

    public abstract void relaod();
}
