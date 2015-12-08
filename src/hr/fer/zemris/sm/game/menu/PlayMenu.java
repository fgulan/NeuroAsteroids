package hr.fer.zemris.sm.game.menu;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import static hr.fer.zemris.sm.game.Constants.*;

/**
 *
 * Created by doctor on 03.12.15..
 */
public class PlayMenu extends Menu {

    public PlayMenu(Game parent) {
        super(parent);

        VBox buttons = createButtons();
        getChildren().add(buttons);
    }

    private VBox createButtons() {
        VBox buttons = new VBox();
        buttons.setId(PLAY_MENU_BUTTONS);

        Button humanPlay = new KeyEventButton(HUMAN_PLAY_BTN_TEXT);
        humanPlay.setOnAction(e-> {
            new GameStart().handle(null);
        });

        Button AIPlay = new KeyEventButton(AI_PLAY_BTN_TEXT);
        AIPlay.setOnAction(e -> {
            Game parent = getGameParent();
            parent.transition(this, parent.getAIChooserMenu(), Game.LEFT);
        });

        Button back = new KeyEventButton(BACK_BUTTON_TEXT);
        back.setOnAction(e -> {
            Game parent = getGameParent();
            parent.transition(this, parent.getStartMenu(), Game.RIGHT);
        });

        buttons.getChildren().addAll(humanPlay, AIPlay, back);

        return buttons;
    }
}
