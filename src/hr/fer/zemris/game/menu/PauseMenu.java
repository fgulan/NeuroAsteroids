package hr.fer.zemris.game.menu;

import hr.fer.zemris.game.sound.BackgroundSoundManager;
import hr.fer.zemris.game.sound.EffectsSoundManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import static hr.fer.zemris.game.Constants.*;

/**
 *
 * Created by doctor on 05.12.15..
 */
public class PauseMenu extends Menu {

    private EventHandler<ActionEvent> resumeAction;

    public PauseMenu(Game parent) {
        super(parent);
        resumeAction = new ResumeAction();
        setId(PAUSE_MENU);

        Pane pausePane = createPausePane();
        pausePane.setId(PAUSE_PANE);

        Button exit = new KeyEventButton(PAUSE_MENU_EXIT_BUTTON_TEXT);
        exit.setId(PAUSE_MENU_EXIT_BUTTON);
        exit.setOnAction( e -> {
            parent.getRoot().getChildren().remove(parent.getPauseMenu());
        });

        setCenter(pausePane);
        setBottom(exit);
    }

    private Pane createPausePane() {
        HBox pane = new HBox();

        Button resume = new KeyEventButton(PAUSE_MENU_RESUME_BUTTON_TEXT);
        resume.setId(PAUSE_MENU_RESUME_BUTTON);
        resume.setOnAction( e -> {
            Game parent = getGameParent();
            parent.getRoot().getChildren().remove(parent.getPauseMenu());
            //TODO: continue game
        });

        Button restart = new KeyEventButton(PAUSE_MENU_RESTART_BUTTON_TEXT);
        restart.setId(PAUSE_MENU_RESTART_BUTTON);
        restart.setOnAction( e -> {
            Game parent = getGameParent();
            parent.getRoot().getChildren().remove(parent.getPauseMenu());

            //TODO: restart a game
        });

        VBox buttons = new VBox();
        buttons.setId(PAUSE_PANE_BUTTONS);
        buttons.getChildren().addAll(resume, restart);

        VBox sliders = new VBox();
        sliders.setId(PAUSE_PANE_SLIDERS);
        sliders.getChildren().add(new Label(BACKGROUND_SOUND_LABEL_TEXT));

        Slider backgroundSoundSlider = new Slider();
        backgroundSoundSlider.setMin(0);
        backgroundSoundSlider.setMax(1);
        backgroundSoundSlider.valueProperty().addListener( e -> {
            BackgroundSoundManager.getInstance().setVolume( backgroundSoundSlider.getValue() );
        });
        sliders.getChildren().add(backgroundSoundSlider);

        sliders.getChildren().add(new Separator());

        sliders.getChildren().add(new Label(EFFECT_SOUND_LABEL_TEXT));
        Slider effectSoundSlider = new Slider();
        effectSoundSlider.setMin(0);
        effectSoundSlider.setMax(1);
        effectSoundSlider.valueProperty().addListener( e -> {
            EffectsSoundManager.getInstance().setVolume( effectSoundSlider.getValue() );
        });
        sliders.getChildren().add(effectSoundSlider);

        pane.getChildren().addAll(buttons, sliders);

        return pane;
    }
    private class ResumeAction implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            System.out.println("resumed");
        }
    }
}

