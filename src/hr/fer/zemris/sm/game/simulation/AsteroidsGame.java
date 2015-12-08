package hr.fer.zemris.sm.game.simulation;

import hr.fer.zemris.sm.game.controllers.KeyboardController;
import hr.fer.zemris.sm.game.world.Listeners.GameOverListener;
import hr.fer.zemris.sm.game.world.GraphicsWorld;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AsteroidsGame extends Application implements GameOverListener {

    private static int WIDTH = 800;
    private static int HEIGHT = 600;

    GraphicsWorld gameWorld;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        gameWorld.initialize();
        Group gameSurface = gameWorld.getGameSurface();
        Scene game = new Scene(gameSurface, WIDTH, HEIGHT);
        primaryStage.setScene(game);
        KeyboardController controller = new KeyboardController(game);
        primaryStage.show();
        gameWorld.registerGameOverListener(this);
        gameWorld.play();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void gameOver() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Game over");
        alert.setHeaderText("Looser");
        alert.setContentText("Dude, you suck!");
        //alert.show();
    }
}
