package hr.fer.zemris.game.menu;

import hr.fer.zemris.game.sound.BackgroundSoundManager;
import hr.fer.zemris.game.sound.EffectsSoundManager;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import static hr.fer.zemris.game.Constants.*;

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
    private Menu AITrainMenu;
    private Menu pauseMenu;

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

        Image cursorImage = new Image(ClassLoader.getSystemResourceAsStream(CURSOR_IMG_PATH));
        ImageCursor cursor = new ImageCursor(cursorImage, cursorImage.getWidth() / 2, cursorImage.getHeight() / 2);
        scene.setCursor(cursor);

        Image gameIcon = new Image(ClassLoader.getSystemResourceAsStream("res/Meteor-icon.png"));
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
        AITrainMenu = new AITrainMenu(this);
        pauseMenu = new PauseMenu(this);
    }

    public void transition(Pane from, Pane to, int direction) {
        to.setOpacity(0);   //Make invisible for fade in animation, so it doesn't glitch
        root.getChildren().add(0, to);

        TranslateTransition fTrans = new TranslateTransition(Duration.seconds(OUT_TRANSITION_DURNATION), from);
        fTrans.setFromX(0);
        fTrans.setToX(direction * stage.getWidth());

        TranslateTransition tTrans = new TranslateTransition(Duration.seconds(IN_TRANSITION_DURNATION), to);
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
        Platform.exit();
    }

    public Pane getStartMenu() {
        return startMenu;
    }

    public Pane getPlayMenu() {
        return playMenu;
    }

    public Pane getScoresMenu() {
        return scoresMenu;
    }

    public Pane getOptionsMenu() {
        return optionsMenu;
    }

    public Pane getCreditsMenu() {
        return creditsMenu;
    }

    public Pane getAIChooserMenu() {
        return AIChooserMenu;
    }

    public Pane getAITrainMenu() {
        return AITrainMenu;
    }

    public Pane getPauseMenu() {
        return pauseMenu;
    }

    public Pane getGame() {
        return null;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
