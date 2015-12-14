package hr.fer.zemris.sm.game.menu;

import hr.fer.zemris.sm.game.Utils.HSDataUtility;
import hr.fer.zemris.sm.game.Utils.ScoreElement;
import hr.fer.zemris.sm.game.menu.menuUtil.KeyEventButton;
import hr.fer.zemris.sm.game.menu.menuUtil.ScoreEnteredListener;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

import static hr.fer.zemris.sm.game.Constants.*;

/**
 *
 * Created by Fredi Šarić on 13.12.15.
 */
public class HighScoreScreen extends BorderPane {

    private static final int OK = 1;

    private static final int TAKEN = 2;

    private static final int TOO_LONG = 3;

    private static final int EMPTY = 4;

    private List<ScoreEnteredListener> listeners;

    public HighScoreScreen(int score, int type) {
        setId(HIGH_SCORE_SCREEN);
        VBox box = new VBox();
        box.setId(HIGH_SCREEN_CONTENT_BOX);

        addLabels(box, score);

        TextField nameField = new TextField();
        nameField.setId(NAME_TEXT_FIELD);

        Label wrongInputLabel = new Label();
        wrongInputLabel.setId(WRONG_INPUT_LABEL);

        HBox hBox = new HBox();
        hBox.setId(SCORE_INPUT_HBOX);
        hBox.getChildren().addAll(nameField, wrongInputLabel);
        box.getChildren().add(hBox);

        Button addButton = new KeyEventButton(ENTERED_SCORE_BUTTON_TEXT);
        addButton.setId(ENTERED_SCORE_BUTTON);
        addButton.setOnAction( e -> {
            int res = checkName(nameField.getText());
            if(res == OK) {
                ScoreElement se = new ScoreElement(type, nameField.getText(), (double) score);
                HSDataUtility.getInstance().addNewHighScore(se);
                HSDataUtility.getInstance().flush(); //Saved

                fire();
            } else if(res == TAKEN){
                wrongInputLabel.setText(NAME_TAKEN_TEXT);
            } else if(res == TOO_LONG) {
                wrongInputLabel.setText(NAME_TOO_LONG_TEXT);
            } else if(res == EMPTY) {
                wrongInputLabel.setText(NAME_EMPTY_TEXT);
            }
        });
        box.getChildren().add(addButton);

        setCenter(box);
    }

    private static void addLabels(VBox box, int score) {

        Label highScoreLabel = new Label(HIGH_SCORE_LABEL_TEXT);
        highScoreLabel.setId(HIGH_SCORE_LABEL);
        Label scoreLabel = new Label(Integer.toString(score));

        if(score == 1) {
            scoreLabel.setText(scoreLabel.getText() + SCORE_LABEL_TEXT_ONE_POINTS);
        } else {
            scoreLabel.setText(scoreLabel.getText() + SCORE_LABEL_TEXT);
        }

        scoreLabel.setId(SCORE_LABEL);
        Label placeLabel = new Label();
        placeLabel.setId(PLACE_LABEL);

        List<ScoreElement> highScores = HSDataUtility.getInstance().getHighScores();
        highScores.sort((o1, o2) -> (int) Math.signum(o2.getScore() - o1.getScore()));
        for(int i = 0; i <highScores.size(); i++) {
            if(highScores.get(i).getScore() <= score) {
                placeLabel.setText(createPlaceText(i + 1));
                break;
            }
        }

        box.getChildren().addAll(highScoreLabel, placeLabel, scoreLabel);
    }

    private static String createPlaceText(int i) {
        if(i == 11 || i == 12 || i == 13) { //Special case
            return i + "th " + PLACE_TEXT;
        }

        if(i % 10 == 1) {
            return i + "st " + PLACE_TEXT;
        } else if( i % 10 == 2 ) {
            return i + "nd " + PLACE_TEXT;
        } else if( i % 10 == 3){
            return i + "rd " + PLACE_TEXT;
        }

        return i + "th " + PLACE_TEXT;
    }


    private int checkName(String text) {
        if(text.isEmpty()) return EMPTY;
        if(text.length() > MAX_NAME_LENGTH) return TOO_LONG;

        List<ScoreElement> elements = HSDataUtility.getInstance().getHighScores();
        for(ScoreElement se : elements) {
            if( se.getName().equals(text) ) return TAKEN;
        }

        return OK;
    }

    private void fire() {
        if(listeners != null) {
            for(ScoreEnteredListener l : listeners) {
                l.highScoreRegistered();
            }
        }
    }

    public void addScoreEnteredListener(ScoreEnteredListener sel) {
        if(listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(sel);
    }
}
