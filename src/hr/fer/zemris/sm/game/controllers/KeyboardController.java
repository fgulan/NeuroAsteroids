package hr.fer.zemris.sm.game.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyboardController implements IController {

    private Scene gameScene;
    private EventHandler<KeyEvent> pressHandler;
    private EventHandler<KeyEvent> releaseHandler;
    
    private boolean fire;
    private boolean left;
    private boolean right;
    private boolean move;

    public KeyboardController(Scene gameScene) {
        super();
        this.gameScene = gameScene;
    }

    public void register() {
        if (gameScene != null) {
            pressHandler = new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (KeyCode.SPACE == event.getCode()) {
                        fire = true;
                    }
                    if (KeyCode.LEFT == event.getCode()) {
                        left = true;
                    }
                    if (KeyCode.RIGHT == event.getCode()) {
                        right = true;
                    }
                    if (KeyCode.UP == event.getCode()) {
                        move = true;
                    }
                }
            };

            releaseHandler = new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (KeyCode.SPACE == event.getCode()) {
                        fire = false;
                    }
                    if (KeyCode.LEFT == event.getCode()) {
                        left = false;
                    }
                    if (KeyCode.RIGHT == event.getCode()) {
                        right = false;
                    }
                    if (KeyCode.UP == event.getCode()) {
                        move = false;
                    }
                }
            };
            gameScene.addEventHandler(KeyEvent.KEY_PRESSED, pressHandler);
            gameScene.addEventHandler(KeyEvent.KEY_RELEASED, releaseHandler);
        }
    }

    public void deRegister() {
        gameScene.removeEventHandler(KeyEvent.KEY_PRESSED, pressHandler);
        gameScene.removeEventHandler(KeyEvent.KEY_RELEASED, releaseHandler);
    }
    
    @Override
    public List<Input> getInput() {
        List<Input> inputs = new ArrayList<>();
        if (fire) inputs.add(Input.FIRE);
        if (move) inputs.add(Input.MOVE);
        if (left) inputs.add(Input.LEFT);
        if (right) inputs.add(Input.RIGHT);
        return inputs;
    }
}
