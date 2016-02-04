package hr.fer.zemris.sm.game.nodes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import java.util.OptionalInt;

/**
 *
 * Created by doctor on 05.12.15..
 */
public class IntegerNumberInput extends TextField {

    public IntegerNumberInput() {
        this("");
    }

    public IntegerNumberInput(String text) {
        super(text);
        textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue == null || newValue.isEmpty()) {
                    return;
                }
                String lastChar = new Character(newValue.charAt(newValue.length() - 1)).toString();
                if(lastChar.matches("\\d")) {
                    IntegerNumberInput.this.setText(newValue);
                } else {
                    IntegerNumberInput.this.setText(oldValue);
                }
            }
        });
    }

    public OptionalInt getValue() {
        if(getText() == null) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(Integer.parseInt(getText()));
    }
}
