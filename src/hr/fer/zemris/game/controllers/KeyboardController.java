package hr.fer.zemris.game.controllers;

import hr.fer.zemris.game.world.GameWorld;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyboardController {
    
    private static GameWorld gameWorld;
    private static Scene scene;

    public static void register(GameWorld gameWorld, Scene gameScene) {
        KeyboardController.gameWorld = gameWorld;
        setupInput(gameScene);
    }
    
    private static void setupInput(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE) {
                gameWorld.fire(true);
            }
        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.SPACE) {
                gameWorld.fire(false);
            }
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.KP_UP) {
                gameWorld.move(true);
            }
        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.KP_UP) {
                gameWorld.move(false);
            }
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.KP_LEFT) {
                gameWorld.turnLeft(true);
            }
        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.KP_LEFT) {
                gameWorld.turnLeft(false);
            }
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.KP_RIGHT) {
                gameWorld.turnRight(true);
            }
        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.KP_RIGHT) {
                gameWorld.turnRight(false);
            }
        });
        
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.P) {
                gameWorld.pause();
            }
        });
    }
}
