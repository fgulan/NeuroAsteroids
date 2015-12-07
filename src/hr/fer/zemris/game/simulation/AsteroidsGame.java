package hr.fer.zemris.game.simulation;

import hr.fer.zemris.game.controllers.KeyboardController;
import hr.fer.zemris.game.world.GameOverListener;
import hr.fer.zemris.game.world.GraphicsWorld;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AsteroidsGame extends Application implements GameOverListener {

    private static int WIDTH = 800;
    private static int HEIGHT = 600;

    GraphicsWorld gameWorld = new GraphicsWorld(60, WIDTH, HEIGHT, 6);

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        gameWorld.initialize();
        Group gameSurface = gameWorld.getGameSurface();
        Scene game = new Scene(gameSurface, WIDTH, HEIGHT);
        primaryStage.setScene(game);
        primaryStage.show();
        KeyboardController.register(gameWorld, game);
        gameWorld.addListener(this);
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
