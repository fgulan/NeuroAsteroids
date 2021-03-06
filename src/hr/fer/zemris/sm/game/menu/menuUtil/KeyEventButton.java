package hr.fer.zemris.sm.game.menu.menuUtil;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

/**
 * Decorator for button that enables activation on costume key press.
 *
 * Default button activation key is KeyCode.ENTER.
 *
 * Created by doctor on 02.12.15..
 */
public class KeyEventButton extends Button {

    public KeyEventButton(String text) {
        this(KeyCode.ENTER, text);
    }

    public KeyEventButton(KeyCode key) {
        this(key,"");
    }

    public KeyEventButton(KeyCode key, String text) {
        this(key, text, null);
    }

    public KeyEventButton(KeyCode key, String text, Node graphic) {
        super(text, graphic);
        onKeyPressedProperty().setValue(e -> {
            if(e.getCode() == key) {
                setPressed(true);
            }
        });
        onKeyReleasedProperty().setValue(e ->{
            if(e.getCode() == key) {
                setPressed(false);
                fire();
            }
        });
    }
}
