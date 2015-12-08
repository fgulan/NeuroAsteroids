package hr.fer.zemris.game.menu;

import hr.fer.zemris.game.controllers.KeyboardController;
import hr.fer.zemris.game.sound.EffectsSoundManager;
import hr.fer.zemris.game.world.GraphicsWorld;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static hr.fer.zemris.game.Constants.GAME_WORLD_STYLE_PATH;

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

    public class GameStart implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Stage stage = getGameParent().getStage();
            Scene scene = stage.getScene();
            GraphicsWorld world = new GraphicsWorld(60, (int)stage.getWidth(), (int)stage.getHeight(), 20);

            world.registerGameOverListener(() -> {
                EffectsSoundManager.getInstance().playShipExploded();

                world.pause();
                StackPane pane = new StackPane();
                pane.getChildren().addAll(scene.getRoot(), new GameOverScreen(getGameParent()));
                scene.setRoot(pane);
            });

            world.registerFireListener(() -> {
                EffectsSoundManager.getInstance().playFire();
            });

            world.registerExplosionListener(() -> {
                EffectsSoundManager.getInstance().playExplosion();
            });
            world.initialize();
            KeyboardController.register(world, scene);
            Pane gameSurface = new Pane(world.getGameSurface());
            scene.setRoot(gameSurface);
            scene.getStylesheets().add(ClassLoader.getSystemResource(GAME_WORLD_STYLE_PATH).toExternalForm());
            world.play();
        }
    }

}
