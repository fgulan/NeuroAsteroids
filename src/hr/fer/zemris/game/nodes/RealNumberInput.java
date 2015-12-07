package hr.fer.zemris.game.nodes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import java.util.OptionalDouble;

/**
 * Created by doctor on 05.12.15..
 */
public class RealNumberInput extends TextField {
    public RealNumberInput() {
        this("");
    }

    public RealNumberInput(String text) {
        super(text);

        textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue == null || newValue.length() == 0) {
                    return;
                }
                String lastChar = new Character(newValue.charAt(newValue.length() - 1)).toString();
                if(lastChar.matches("\\d")) {
                    RealNumberInput.this.setText(newValue);
                } else if(lastChar.equals(".") &&  //If its a dot
                        newValue.length() != 1 &&  //and if its not a first character
                        !newValue.substring(0, newValue.length() - 1).contains(".")) //adn if its an only dot
                {
                    RealNumberInput.this.setText(newValue);
                } else {
                    RealNumberInput.this.setText(oldValue);
                }
            }
        });
    }

    public OptionalDouble getValue() {
        if(getText() == null) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of(Double.parseDouble(getText()));
    }
}



