package hr.fer.zemris.sm.game.menu;

import hr.fer.zemris.sm.game.controllers.IConnectibleController;
import hr.fer.zemris.sm.game.controllers.MultipleKeyController;
import hr.fer.zemris.sm.game.sound.BackgroundSoundManager;
import hr.fer.zemris.sm.game.sound.EffectsSoundManager;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import static hr.fer.zemris.sm.game.Constants.*;
import static javafx.scene.input.KeyCode.*;

/**
 *
 * Created by doctor on 02.12.15..
 */
public class Game extends Application {

    public static final int RIGHT = 1;
    public static final int LEFT  =-1;

    private Menu startMenu;
    private Menu playMenu;
    private Menu scoresMenu;
    private Menu optionsMenu;
    private Menu creditsMenu;
    private Menu AIChooserMenu;

    private IConnectibleController humanArrowController;
    private IConnectibleController humanWASDController;

    private IConnectibleController chosenController;

    private ImageCursor cursor;

    private Stage stage;
    private StackPane root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BackgroundSoundManager.getInstance().loopBackground();
        BackgroundSoundManager.getInstance().setVolume(INIT_BACKGROUND_SOUND_VOLUME);

        EffectsSoundManager.getInstance().setVolume(INIT_EFFECT_SOUND_VOLUME);

        createMenus();

        stage = primaryStage;
        root = new StackPane();
        root.getChildren().add(startMenu);
        root.setId("Root");

        Scene scene = new Scene(root, 1000, 1000);
        scene.getStylesheets().add(ClassLoader.getSystemResource(GAME_STYLE_PATH).toExternalForm());

        humanArrowController = new MultipleKeyController(scene ,UP, KeyCode.LEFT, KeyCode.RIGHT, SPACE);
        humanWASDController  = new MultipleKeyController(scene ,W , A, D, SPACE);
        chosenController = humanArrowController;

        Image cursorImage = new Image(ClassLoader.getSystemResourceAsStream(CURSOR_IMG_PATH));
        cursor = new ImageCursor(cursorImage, cursorImage.getWidth() / 2, cursorImage.getHeight() / 2);
        scene.getRoot().setCursor(cursor);

        Image gameIcon = new Image(ClassLoader.getSystemResourceAsStream(GAME_ICON));
        stage.getIcons().add(gameIcon);

        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }

    private void createMenus() {
        startMenu = new StartMenu(this);
        playMenu = new PlayMenu(this);
        scoresMenu = new ScoreMenu(this);
        creditsMenu = new CreditsMenu(this);
        optionsMenu = new OptionsMenu(this);
        AIChooserMenu = new AIChooserMenu(this);
    }

    public void transition(Pane from, Pane to, int direction) {
        to.setOpacity(0);   //Make invisible for fade in animation, so it doesn't glitch
        root.getChildren().add(0, to);

        TranslateTransition fTrans = new TranslateTransition(Duration.seconds(OUT_TRANSITION_DURATION), from);
        fTrans.setFromX(0);
        fTrans.setToX(direction * stage.getWidth());

        TranslateTransition tTrans = new TranslateTransition(Duration.seconds(IN_TRANSITION_DURATION), to);
        tTrans.setFromX(-direction * stage.getWidth());
        tTrans.setToX(0);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), to);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ParallelTransition trans = new ParallelTransition(fadeIn, tTrans);

        fTrans.setOnFinished(e -> {
            root.getChildren().remove(from);
            trans.play();
        });

        fTrans.play();
    }

    public Stage getStage() {
        return stage;
    }

    public StackPane getRoot() {
        return root;
    }

    public void closeGame() {
        humanArrowController.disconnect();
        startMenu.disconnect();
        playMenu.disconnect();
        scoresMenu.disconnect();
        optionsMenu.disconnect();
        AIChooserMenu.disconnect();
        creditsMenu.disconnect();

        Platform.exit();
    }

    public Pane getStartMenu() {
        startMenu.reload();
        return startMenu;
    }

    public Pane getPlayMenu() {
        playMenu.reload();
        return playMenu;
    }

    public Pane getScoresMenu() {
        scoresMenu.reload();
        return scoresMenu;
    }

    public Pane getOptionsMenu() {
        optionsMenu.reload();
        return optionsMenu;
    }

    public Pane getCreditsMenu() {
        creditsMenu.reload();
        return creditsMenu;
    }

    public Pane getAIChooserMenu() {
        AIChooserMenu.reload();
        return AIChooserMenu;
    }

    public void hideCursor() {
        stage.getScene().setCursor(Cursor.NONE);
    }

    public IConnectibleController getHumanController() {
        return chosenController;
    }

    public IConnectibleController getHumanArrowController() {
        return humanArrowController;
    }

    public IConnectibleController getHumanWASDController() {
        return humanWASDController;
    }

    public void setHumanController(IConnectibleController controller) {
        chosenController = controller;
    }

    public void showCursor() {
        stage.getScene().setCursor(cursor);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
