package hr.fer.zemris.sm.game.menu;

import hr.fer.zemris.sm.game.Constants;
import hr.fer.zemris.sm.game.menu.menuUtil.KeyEventButton;
import hr.fer.zemris.sm.game.sound.BackgroundSoundManager;
import hr.fer.zemris.sm.game.sound.EffectsSoundManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import static hr.fer.zemris.sm.game.Constants.*;


/**
 *
 * Created by doctor on 03.12.15..
 */
public class OptionsMenu extends Menu {

    public Slider backgroundSoundSlider;
    public Slider effectSoundSlider;

    public OptionsMenu(Game parent) {
        super(parent);

        Pane optionsPane = createOptionsPane();

        Button back = new KeyEventButton(BACK_BUTTON_TEXT);
        back.setId(OPTIONS_MENU_BACK_BUTTON);
        back.setOnAction(e -> {
            parent.transition(this, parent.getStartMenu(), Game.RIGHT);
        });

        setCenter(optionsPane);
        setBottom(back);
    }

    private Pane createOptionsPane() {
        VBox optionsPane = new VBox();
        optionsPane.setId(OPTIONS_PANE);

        VBox audioBox = new VBox();
        Label audioLabel = new Label(AUDIO_LABEL_TEXT);
        audioLabel.setId(AUDIO_LABEL);
        Pane audioPane = createAudioPane();
        audioBox.getChildren().addAll(audioLabel, audioPane);

        VBox videoBox = new VBox();
        Label videoLabel = new Label(VIDEO_LABEL_TEXT);
        videoLabel.setId(VIDEO_LABEL);
        Pane videoPane = createVideoPane();
        videoBox.getChildren().addAll(videoLabel, videoPane);

        VBox controlBox = new VBox();
        Label controlLabel = new Label(CONTROL_LABEL_TEXT);
        controlLabel.setId(CONTROL_LABEL);
        Pane controlPane = createControlPane();
        controlBox.getChildren().addAll(controlLabel, controlPane);

        optionsPane.getChildren().addAll(audioBox, videoBox, controlBox);

        return optionsPane;
    }

    private Pane createControlPane() {
        GridPane grid = createGridPane();
        grid.setId(CONTROL_OPTIONS_GRID);

        Label controlInputLabel = new Label(CONTROL_INPUT_LABEL_TEXT);
        ToggleGroup group = new ToggleGroup();

        RadioButton arrows = new RadioButton();
        arrows.setToggleGroup(group);
        Label arrowsControlLabel = new Label(ARROW_CONTROL_INPUT_LABEL_TEXT);
        grid.addRow(0, arrows, arrowsControlLabel);

        grid.add(new Separator(), 0, 1, 2, 1);

        RadioButton wasd = new RadioButton();
        wasd.setToggleGroup(group);
        Label wasdControls = new Label(WASD_CONTROL_INPUT_LABEL_TEXT);
        grid.addRow(2, wasd, wasdControls);

        grid.add(new Separator(), 0, 1, 2, 1);

        if(getGameParent().getHumanController() == getGameParent().getHumanArrowController()) {
            arrows.setSelected(true);
        } else {
            wasd.setDisable(false);
        }

        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if(wasd.isDisabled()) {
                getGameParent().setHumanController(getGameParent().getHumanArrowController());
            } else {
                getGameParent().setHumanController(getGameParent().getHumanWASDController());
            }
        });

        return grid;
    }

    private Pane createAudioPane() {
        GridPane grid = createGridPane();
        grid.setId(AUDIO_OPTIONS_GRID);

        Label backgroundSoundLabel = new Label(BACKGROUND_SOUND_LABEL_TEXT);
        backgroundSoundSlider = new Slider();
        backgroundSoundSlider.setMin(0); //Minimal sound 0%
        backgroundSoundSlider.setMax(1); //Maximal sound 100%
        backgroundSoundSlider.setValue(BackgroundSoundManager.getInstance().getVolume());

        backgroundSoundSlider.valueProperty().addListener( e -> {
            BackgroundSoundManager.getInstance().setVolume( backgroundSoundSlider.getValue() );
        });

        grid.addRow(0, backgroundSoundLabel, backgroundSoundSlider);
        addSeparator(grid, 1);

        Label effectSoundLabel = new Label(EFFECT_SOUND_LABEL_TEXT);
        effectSoundSlider = new Slider();
        effectSoundSlider.setValue(EffectsSoundManager.getInstance().getVolume());
        effectSoundSlider.setMin(0);
        effectSoundSlider.setMax(1);

        effectSoundSlider.valueProperty().addListener( e ->  {
            EffectsSoundManager.getInstance().setVolume(effectSoundSlider.getValue());
        });

        grid.addRow(2, effectSoundLabel, effectSoundSlider);
        addSeparator(grid, 3);

        return grid;
    }

    private void addSeparator(GridPane grid, int row) {
        Separator s = new Separator();
        grid.add(s,0,row,2,1);
    }

    private Pane createVideoPane() {
        //TODO: make video pane
        return new Pane();
    }

    private Pane createGameWorldPane() {
        GridPane grid = createGridPane();
        grid.setId(GAME_WORLD_OPTIONS_GRID);

        Label numOfStarsLabel =  new Label(GAME_WORLD_STARS_ON_SCREEN);
        Slider numOfStarsSlider = new Slider();
        numOfStarsSlider.setMin(1);
        numOfStarsSlider.setMax(10);
        numOfStarsSlider.setValue(ASTEROIDS_NUMEBER);
        numOfStarsSlider.setBlockIncrement(1);

        grid.addRow(0, numOfStarsLabel, numOfStarsSlider);
        addSeparator(grid, 1);

        Label numOfAsteroidsLabel =  new Label(GAME_WORLD_ASTEROIDS_ON_SCREEN);
        Slider numOfAsteroidsOnScreen =  new Slider();
        numOfAsteroidsOnScreen.setMin(0);
        numOfAsteroidsOnScreen.setMax(15);
        numOfAsteroidsOnScreen.setValue(ASTEROIDS_NUMEBER);
        numOfAsteroidsOnScreen.setBlockIncrement(1);

        grid.addRow(2, numOfAsteroidsLabel, numOfAsteroidsOnScreen);
        addSeparator(grid, 3);


        Label fuelIncreaseLabel = new Label(GAME_WORLD_FUEL_INCREASE_TEXT);
        Slider fuelIncreaseSlider = new Slider();
        fuelIncreaseSlider.setMin(500);
        fuelIncreaseSlider.setMax(5000);
        fuelIncreaseSlider.setValue(STAR_FUEL);
        fuelIncreaseSlider.setBlockIncrement(10);


        grid.addRow(0, fuelIncreaseLabel, fuelIncreaseSlider);
        addSeparator(grid, 1);

        Label ammoIncreaseLabel = new Label(GAME_WORLD_AMMO_INCREASE_TEXT);
        Slider ammoIncreaseSlider = new Slider();
        ammoIncreaseSlider.setMin(2);
        ammoIncreaseSlider.setMax(15);
        ammoIncreaseSlider.setValue(STAR_MISSILE);
        ammoIncreaseSlider.setBlockIncrement(1);

        grid.addRow(2, ammoIncreaseLabel, ammoIncreaseSlider);
        addSeparator(grid, 3);

        return grid;
    }

    private GridPane createGridPane() {
        GridPane pane = new GridPane();

        ColumnConstraints labels = new ColumnConstraints();
        labels.setPercentWidth(50);
        labels.setHalignment(HPos.LEFT);

        ColumnConstraints sliders = new ColumnConstraints();
        sliders.setHalignment(HPos.RIGHT);
        sliders.setPercentWidth(50);

        sliders.setHgrow(Priority.ALWAYS);

        pane.getColumnConstraints().addAll(labels, sliders);

        return pane;
    }

    @Override
    public void relaod() {
        backgroundSoundSlider.setValue(BackgroundSoundManager.getInstance().getVolume());
        effectSoundSlider.setValue(EffectsSoundManager.getInstance().getVolume());
    }
}
