package hr.fer.zemris.sm.game.menu;

import hr.fer.zemris.sm.game.Constants;
import hr.fer.zemris.sm.game.menu.menuUtil.KeyEventButton;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.nio.file.Paths;

/**
 * Main menu congaing all of the navigation buttons needed to access the other menus.
 *
 * Created by doctor on 02.12.15..
 */
public class StartMenu extends Menu {

    public StartMenu(Game parent) {
        super(parent);
        setId(Constants.START_MENU_ID);

        ImageView title = new ImageView();
        InputStream src = Menu.class.getClassLoader().getResourceAsStream(Constants.GAME_TITLE);
        if(src != null) {
            title.setImage(new Image(src));
        }

        title.setId(Constants.GAME_TITLE_LABEL);

        VBox buttons = createButtons();
        BorderPane pane = new BorderPane();
        pane.setTop(title);
        pane.setLeft(buttons);
        setCenter(pane);
    }

    private VBox createButtons() {
        VBox buttons = new VBox();
        buttons.setId(Constants.START_MENU_BUTTONS_DIV);

        Button play = createPlayButton();
        Button scores = createScoresButton();
        Button options = createOptionsMenu();
        Button credits = createCreditsMenu();
        Button exit = createExitMenu();

        buttons.getChildren().addAll(play, scores, options, credits, exit);
        return buttons;
    }

    private Button createExitMenu() {
        Button exit = new KeyEventButton(Constants.EXIT_BUTTON_TEXT);
        exit.setId(Constants.EXIT_BUTTON);
        exit.setOnAction(e-> getGameParent().closeGame());
        return exit;
    }

    private Button createCreditsMenu() {
        Button credits = new KeyEventButton(Constants.CREDITS_BUTTON_TEXT);
        credits.setId(Constants.CREDITS_BUTTON);
        credits.setOnAction(e -> {
            Game parent = getGameParent();
            parent.transition(this, parent.getCreditsMenu(), Game.LEFT);
        });
        return credits;
    }

    private Button createOptionsMenu() {
        Button options = new KeyEventButton(Constants.OPTIONS_BUTTON_TEXT);
        options.setId(Constants.OPTIONS_BUTTON);
        options.setOnAction(e -> {
            Game parent = getGameParent();
            parent.transition(this, parent.getOptionsMenu(), Game.LEFT);
        });
        return options;
    }

    private Button createScoresButton() {
        Button scores = new KeyEventButton(Constants.SCORES_BUTTON_TEXT);
        scores.setId(Constants.SCORES_BUTTON);
        scores.setOnAction(e -> {
            Game parent = getGameParent();
            parent.transition(this, parent.getScoresMenu(), Game.LEFT);
        });
        return scores;
    }

    private Button createPlayButton() {
        Button play = new KeyEventButton(Constants.PLAY_BUTTON_TEXT);
        play.setId(Constants.PLAY_BUTTON);
        play.setOnAction(e -> {
            Game parent = getGameParent();
            parent.transition(this, parent.getPlayMenu(), Game.LEFT);
        });
        return play;
    }

    @Override
    public void reload() {
        //No implementation
    }
}
