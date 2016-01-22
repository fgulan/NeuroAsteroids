package hr.fer.zemris.sm.game.menu;

import hr.fer.zemris.sm.game.menu.menuUtil.KeyEventButton;
import hr.fer.zemris.sm.game.sound.BackgroundSoundManager;
import hr.fer.zemris.sm.game.sound.EffectsSoundManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import static hr.fer.zemris.sm.game.Constants.*;

/**
 *
 * Created by doctor on 05.12.15..
 */
public class PauseMenu extends Menu {

    private EventHandler<ActionEvent> resumeAction;

    Button exit;

    Button resume;

    Button restart;

    Slider  backgroundSoundSlider;

    Slider effectSoundSlider;

    public PauseMenu(Game parent) {
        super(parent);
        resumeAction = new ResumeAction();
        setId(PAUSE_MENU);

        Pane pausePane = createPausePane();
        pausePane.setId(PAUSE_PANE);

        exit = new KeyEventButton(PAUSE_MENU_EXIT_BUTTON_TEXT);
        exit.setId(PAUSE_MENU_EXIT_BUTTON);

        setCenter(pausePane);
        setBottom(exit);
    }

    private Pane createPausePane() {
        HBox pane = new HBox();

        resume = new KeyEventButton(PAUSE_MENU_RESUME_BUTTON_TEXT);
        resume.setId(PAUSE_MENU_RESUME_BUTTON);

        restart = new KeyEventButton(PAUSE_MENU_RESTART_BUTTON_TEXT);
        restart.setId(PAUSE_MENU_RESTART_BUTTON);

        VBox buttons = new VBox();
        buttons.setId(PAUSE_PANE_BUTTONS);
        buttons.getChildren().addAll(resume, restart);

        VBox sliders = new VBox();
        sliders.setId(PAUSE_PANE_SLIDERS);
        sliders.getChildren().add(new Label(BACKGROUND_SOUND_LABEL_TEXT));

        backgroundSoundSlider = new Slider();
        backgroundSoundSlider.setMin(0);
        backgroundSoundSlider.setMax(1);
        backgroundSoundSlider.valueProperty().addListener( e -> {
            BackgroundSoundManager.getInstance().setVolume( backgroundSoundSlider.getValue() );
        });
        sliders.getChildren().add(backgroundSoundSlider);

        sliders.getChildren().add(new Separator());

        sliders.getChildren().add(new Label(EFFECT_SOUND_LABEL_TEXT));

        effectSoundSlider = new Slider();
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

    public void setOnResumeAction(EventHandler<ActionEvent> event) {
        resume.setOnAction(event);
    }

    public void setOnExitAction(EventHandler<ActionEvent> event) {
        exit.setOnAction(event);
    }

    public void setOnRestartAction(EventHandler<ActionEvent> event) {
        restart.setOnAction(event);
    }

    @Override
    public void reload() {
        backgroundSoundSlider.setValue(BackgroundSoundManager.getInstance().getVolume());
        effectSoundSlider.setValue(EffectsSoundManager.getInstance().getVolume());
    }
}

