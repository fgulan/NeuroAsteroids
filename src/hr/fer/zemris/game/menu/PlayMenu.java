package hr.fer.zemris.game.menu;

import hr.fer.zemris.game.controllers.KeyboardController;
import hr.fer.zemris.game.world.GameWorld;
import hr.fer.zemris.game.world.GraphicsWorld;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static hr.fer.zemris.game.Constants.*;

/**
 *
 * Created by doctor on 03.12.15..
 */
public class PlayMenu extends Menu {

    public PlayMenu(Game parent) {
        super(parent);

        VBox buttons = createButtons();
        getChildren().add(buttons);
    }

    private VBox createButtons() {
        VBox buttons = new VBox();
        buttons.setId(PLAY_MENU_BUTTONS);

        Button humanPlay = new KeyEventButton(HUMAN_PLAY_BTN_TEXT);
        humanPlay.setOnAction(e-> {
            //TODO: run game
            Stage stage = getGameParent().getStage();
            Scene scene = stage.getScene();
            GraphicsWorld world = new GraphicsWorld(60, (int)stage.getWidth(), (int)getHeight(), 20);
            world.initialize();
            Group gameSurface = world.getGameSurface();

            Pane surface = new Pane();
            surface.getChildren().add(gameSurface);
            scene.setRoot(surface);
            KeyboardController.register(world, scene);
            scene.getStylesheets().add(ClassLoader.getSystemResource("res/gameStyle.css").toExternalForm());
            world.play();
        });


        Button AIPlay = new KeyEventButton(AI_PLAY_BTN_TEXT);
        AIPlay.setOnAction(e -> {
            Game parent = getGameParent();
            parent.transition(this, parent.getAIChooserMenu(), Game.LEFT);
        });

        Button back = new KeyEventButton(BACK_BUTTON_TEXT);
        back.setOnAction(e -> {
            Game parent = getGameParent();
            parent.transition(this, parent.getStartMenu(), Game.RIGHT);
        });

        buttons.getChildren().addAll(humanPlay, AIPlay, back);

        return buttons;
    }
}
