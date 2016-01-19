package hr.fer.zemris.sm.game.controllers;

import hr.fer.zemris.sm.game.world.GameWorld;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fredi Šarić on 18.12.15.
 */
public class MultipleKeyController implements IConnectibleController {

    private boolean move;
    private boolean fire;
    private boolean left;
    private boolean right;

    private List<Input> inputs;

    private Scene scene;

    private KeyPressedController pressedController;
    private KeyReleasedController releasedController;


    public MultipleKeyController(Scene scene, KeyCode move, KeyCode left, KeyCode right, KeyCode fire) {
        this.scene = scene;

        this.releasedController = new KeyReleasedController(move, left, right, fire);
        this.pressedController  = new KeyPressedController(move, left, right, fire);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, pressedController);
        scene.addEventHandler(KeyEvent.KEY_RELEASED, releasedController);

        inputs = new ArrayList<>(4);
    }

    @Override
    public List<Input> getInput() {
        inputs.clear();
        if(move) {
            inputs.add(Input.MOVE);
        }
        if(left) {
            inputs.add(Input.LEFT);
        }
        if(right) {
            inputs.add(Input.RIGHT);
        }
        if(fire) {
            inputs.add(Input.FIRE);
        }

        return inputs;
    }

    @Override
    public void connect() {
        if(scene != null) {
            scene.addEventHandler(KeyEvent.KEY_PRESSED, pressedController);
            scene.addEventHandler(KeyEvent.KEY_RELEASED, releasedController);
        }
    }


    private class KeyPressedController implements EventHandler<KeyEvent> {
        private KeyCode move;
        private KeyCode left;
        private KeyCode right;
        private KeyCode fire;

        public KeyPressedController(KeyCode move, KeyCode left, KeyCode right, KeyCode fire) {
            this.move = move;
            this.left = left;
            this.right = right;
            this.fire = fire;
        }

        @Override
        public void handle(KeyEvent event) {
            KeyCode code = event.getCode();
            if(code.equals(move)) {
                MultipleKeyController.this.move = true;
            } else if(code.equals(left)) {
                MultipleKeyController.this.left = true;
            } else if (code.equals(right)) {
                MultipleKeyController.this.right = true;
            } else if (code.equals(fire)) {
                MultipleKeyController.this.fire = true;
            }
        }
    }

    private class KeyReleasedController implements EventHandler<KeyEvent> {
        private KeyCode move;
        private KeyCode left;
        private KeyCode right;
        private KeyCode fire;

        public KeyReleasedController(KeyCode move, KeyCode left, KeyCode right, KeyCode fire) {
            this.move = move;
            this.left = left;
            this.right = right;
            this.fire = fire;
        }

        @Override
        public void handle(KeyEvent event) {
            KeyCode code = event.getCode();
            if(code.equals(move)) {
                MultipleKeyController.this.move = false;
            } else if(code.equals(left)) {
                MultipleKeyController.this.left = false;
            } else if (code.equals(right)) {
                MultipleKeyController.this.right = false;
            } else if (code.equals(fire)) {
                MultipleKeyController.this.fire = false;
            }
        }
    }

    @Override
    public void setWorld(GameWorld world) {

    }

    @Override
    public void disconnect() {
        if(scene != null) {
            scene.removeEventHandler(KeyEvent.KEY_PRESSED, pressedController);
            scene.removeEventHandler(KeyEvent.KEY_RELEASED, releasedController);
            scene = null;
        }
    }
}
