package hr.fer.zemris.game.menu;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static hr.fer.zemris.game.Constants.*;

/**
 *
 * Created by doctor on 07.12.15..
 */
public class GameOverScreen extends Menu {
    public GameOverScreen(Game parent) {
        super(parent);
        setId(GAME_OVER_SCREEN);

        VBox contentBox = new VBox();
        contentBox.setId(GAME_OVER_CONTENT_BOX);

        Label gameOver = new Label(GAME_OVER_LABEL_TEXT);
        gameOver.setId(GAME_OVER_LABEL);

        HBox buttons = new HBox();
        buttons.setId(GAME_OVER_BUTTON_BOX);
        Button restart = new KeyEventButton(GAME_OVER_RESET_BUTTON_TEXT);
        restart.setId(GAME_OVER_RESET_BUTTON);
        restart.setOnAction(e -> {
            //TODO: Reset game!
        });

        Button toMenu = new KeyEventButton(GAME_OVER_TO_MENU_BUTTON_TEXT);
        toMenu.setId(GAME_OVER_TO_MENU_BUTTON);
        toMenu.setOnAction(e -> {
            //TODO: remove game from root!!
            parent.getRoot().getChildren().remove(this);
            parent.getRoot().getChildren().add(parent.getPlayMenu());
        });

        buttons.getChildren().addAll(restart, toMenu);
        contentBox.getChildren().addAll(gameOver, buttons);
        setCenter(contentBox);
        setAlignment(contentBox, Pos.CENTER);
    }
}
