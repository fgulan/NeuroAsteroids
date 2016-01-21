package hr.fer.zemris.sm.game.menu;

import hr.fer.zemris.sm.game.Constants;
import hr.fer.zemris.sm.game.GameConfig;
import hr.fer.zemris.sm.game.menu.menuUtil.KeyEventButton;
import hr.fer.zemris.sm.game.sound.BackgroundSoundManager;
import hr.fer.zemris.sm.game.sound.EffectsSoundManager;
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

        VBox gameWorldBox = new VBox();
        Label gameWorldLabel = new Label(GAME_WORLD_OPTIONS_LABEL_TEXT);
        gameWorldLabel.setId(GAME_WORLD_OPTIONS_LABEL);
        Pane gameWorldPane = createGameWorldPane();
        gameWorldBox.getChildren().addAll(gameWorldLabel, gameWorldPane);

        VBox audioBox = new VBox();
        Label audioLabel = new Label(AUDIO_LABEL_TEXT);
        audioLabel.setId(AUDIO_LABEL);
        Pane audioPane = createAudioPane();
        audioBox.getChildren().addAll(audioLabel, audioPane);

        VBox controlBox = new VBox();
        Label controlLabel = new Label(CONTROL_LABEL_TEXT);
        controlLabel.setId(CONTROL_LABEL);
        Pane controlPane = createControlPane();
        controlBox.getChildren().addAll(controlLabel, controlPane);

        optionsPane.getChildren().addAll(gameWorldBox, audioBox, controlBox);

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

        addSeparator(grid, 1);

        RadioButton wasd = new RadioButton();
        wasd.setToggleGroup(group);
        Label wasdControls = new Label(WASD_CONTROL_INPUT_LABEL_TEXT);
        grid.addRow(2, wasd, wasdControls);


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

        return grid;
    }

    private Pane createGameWorldPane() {
        GridPane grid = createGridPane();
        grid.setId(GAME_WORLD_OPTIONS_GRID);

        Label numOfStarsLabel =  new Label(GAME_WORLD_STARS_ON_SCREEN);
        Slider numOfStarsSlider = new Slider();
        numOfStarsSlider.setMin(1);
        numOfStarsSlider.setMax(10);
        numOfStarsSlider.setValue(STARS_NUMBER);
        numOfStarsSlider.setBlockIncrement(1);
        numOfStarsSlider.setMajorTickUnit(1);
        numOfStarsSlider.setSnapToTicks(true);
        numOfStarsSlider.valueProperty().addListener( e -> {
            GameConfig.getInstance().setNumberOfStars((int) numOfStarsSlider.getValue());
        });

        grid.addRow(0, numOfStarsLabel, numOfStarsSlider);
        addSeparator(grid, 1);

        Label numOfAsteroidsLabel =  new Label(GAME_WORLD_ASTEROIDS_ON_SCREEN);
        Slider numOfAsteroidsOnScreen =  new Slider();
        numOfAsteroidsOnScreen.setMin(0);
        numOfAsteroidsOnScreen.setMax(15);
        numOfAsteroidsOnScreen.setValue(ASTEROIDS_NUMEBER);
        numOfAsteroidsOnScreen.setBlockIncrement(1);
        numOfAsteroidsOnScreen.setMajorTickUnit(1);
        numOfAsteroidsOnScreen.setSnapToTicks(true);
        numOfAsteroidsOnScreen.valueProperty().addListener( e -> {
            GameConfig.getInstance().setNumberOfComments((int) numOfAsteroidsOnScreen.getValue());
        });

        grid.addRow(2, numOfAsteroidsLabel, numOfAsteroidsOnScreen);
        addSeparator(grid, 3);

        Label fuelIncreaseLabel = new Label(GAME_WORLD_FUEL_INCREASE_TEXT);
        Slider fuelIncreaseSlider = new Slider();
        fuelIncreaseSlider.setMin(500);
        fuelIncreaseSlider.setMax(5000);
        fuelIncreaseSlider.setValue(STAR_FUEL);
        fuelIncreaseSlider.setBlockIncrement(10);
        fuelIncreaseSlider.setMajorTickUnit(10);
        fuelIncreaseSlider.setSnapToTicks(true);
        fuelIncreaseSlider.valueProperty().addListener( e -> {
            GameConfig.getInstance().setFuelIncrease((int) fuelIncreaseSlider.getValue());
        });

        grid.addRow(4, fuelIncreaseLabel, fuelIncreaseSlider);
        addSeparator(grid, 5);

        Label ammoIncreaseLabel = new Label(GAME_WORLD_AMMO_INCREASE_TEXT);
        Slider ammoIncreaseSlider = new Slider();
        ammoIncreaseSlider.setMin(2);
        ammoIncreaseSlider.setMax(15);
        ammoIncreaseSlider.setValue(STAR_MISSILE);
        ammoIncreaseSlider.setBlockIncrement(1);
        ammoIncreaseSlider.setMajorTickUnit(1);
        ammoIncreaseSlider.setSnapToTicks(true);
        ammoIncreaseSlider.valueProperty().addListener( e -> {
            GameConfig.getInstance().setAmmoIncrease((int) ammoIncreaseSlider.getValue());
        });

        grid.addRow(6, ammoIncreaseLabel, ammoIncreaseSlider);
        addSeparator(grid, 7);

        Label accelerationLabel = new Label(GAME_WORLD_ACCELERATIN_LABEL_TEXT);
        Slider accelerationSlider = new Slider();
        accelerationSlider.setMin(0);
        accelerationSlider.setMax(1);
        accelerationSlider.setValue(Constants.ACCELERATION_STEP);
        accelerationSlider.setBlockIncrement(0.01);
        accelerationSlider.setMajorTickUnit(0.01);
        accelerationSlider.setSnapToTicks(true);
        accelerationSlider.valueProperty().addListener( e -> {
            GameConfig.getInstance().setAcceleration(accelerationSlider.getValue());
        });

        grid.addRow(8, accelerationLabel, accelerationSlider);
        addSeparator(grid, 9);

        Label decelerationLabel = new Label("Deceleration");
        Slider decelerationSlider = new Slider();
        decelerationSlider.setMin(0);
        decelerationSlider.setMax(1);
        decelerationSlider.setValue(Constants.DECELERATION_STEP);
        decelerationSlider.setBlockIncrement(0.01);
        decelerationSlider.setMajorTickUnit(0.01);
        decelerationSlider.setSnapToTicks(true);
        decelerationSlider.valueProperty().addListener( e -> {
            GameConfig.getInstance().setDeceleration(decelerationSlider.getValue());
        });

        grid.addRow(10, decelerationLabel, decelerationSlider);


        return grid;
    }

    private void addSeparator(GridPane grid, int row) {
        Separator s = new Separator();
        grid.add(s,0,row,2,1);
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
