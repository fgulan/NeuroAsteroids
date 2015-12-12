package hr.fer.zemris.sm.game.menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;

import static hr.fer.zemris.sm.game.Constants.*;

/**
 *
 * Created by doctor on 07.12.15..
 */
public class GameOverScreen extends Menu {
    Button toMenu;
    Button restart;

    public GameOverScreen(Game parent) {
        super(parent);
        setId(GAME_OVER_SCREEN);

        VBox contentBox = new VBox();
        contentBox.setId(GAME_OVER_CONTENT_BOX);

        Label gameOver = new Label(GAME_OVER_LABEL_TEXT);
        gameOver.setId(GAME_OVER_LABEL);

        HBox buttons = new HBox();
        buttons.setId(GAME_OVER_BUTTON_BOX);
        restart = new KeyEventButton(GAME_OVER_RESET_BUTTON_TEXT);
        restart.setId(GAME_OVER_RESET_BUTTON);

        toMenu = new KeyEventButton(GAME_OVER_TO_MENU_BUTTON_TEXT);
        toMenu.setId(GAME_OVER_TO_MENU_BUTTON);


        buttons.getChildren().addAll(restart, toMenu);
        contentBox.getChildren().addAll(gameOver, buttons);
        setCenter(contentBox);
        setAlignment(contentBox, Pos.CENTER);
    }

    @Override
    public void relaod() {
        //No implementation
    }

    public void setOnRestartAction(EventHandler<ActionEvent> value) {
        restart.setOnAction(value);
    }

    public void setToMenuAction(EventHandler<ActionEvent> value) {
        toMenu.setOnAction(value);
    }

}
