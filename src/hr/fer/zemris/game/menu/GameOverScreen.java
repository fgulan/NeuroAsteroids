package hr.fer.zemris.game.menu;

import hr.fer.zemris.game.controllers.KeyboardController;
import hr.fer.zemris.game.sound.EffectsSoundManager;
import hr.fer.zemris.game.world.GraphicsWorld;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
            new GameStart().handle(null);
        });

        Button toMenu = new KeyEventButton(GAME_OVER_TO_MENU_BUTTON_TEXT);
        toMenu.setId(GAME_OVER_TO_MENU_BUTTON);
        toMenu.setOnAction(e -> {
            //TODO: deregister key listener
            Pane root = parent.getRoot();
            root.getChildren().clear();
            root.getChildren().add(parent.getPlayMenu());
            Scene scene = parent.getStage().getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(ClassLoader.getSystemResource(GAME_STYLE_PATH).toExternalForm());
            scene.setRoot(root);
        });

        buttons.getChildren().addAll(restart, toMenu);
        contentBox.getChildren().addAll(gameOver, buttons);
        setCenter(contentBox);
        setAlignment(contentBox, Pos.CENTER);
    }
}
