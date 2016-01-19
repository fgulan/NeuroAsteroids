package hr.fer.zemris.sm.game.menu;

import hr.fer.zemris.sm.game.GameConfig;
import hr.fer.zemris.sm.game.Utils.HSDataUtility;
import hr.fer.zemris.sm.game.Utils.ScoreElement;
import hr.fer.zemris.sm.game.controllers.IConnectibleController;
import hr.fer.zemris.sm.game.menu.menuUtil.KeyEventButton;
import hr.fer.zemris.sm.game.sound.EffectsSoundManager;
import hr.fer.zemris.sm.game.world.GraphicsWorld;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static hr.fer.zemris.sm.game.Constants.*;
import static javafx.scene.input.KeyCode.ESCAPE;
import static javafx.scene.input.KeyCode.P;

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
            getGameParent().hideCursor();
            Stage stage = getGameParent().getStage();
            Scene scene = stage.getScene();

            IConnectibleController controller = getGameParent().getHumanController();
            controller.connect(); //Connect to scene

            int asteroidsNumber = GameConfig.getInstance().getNumberOfComments();
            int numberOfStars   = GameConfig.getInstance().getNumberOfStars();

            GraphicsWorld world = new GraphicsWorld(60, (int)stage.getWidth(), (int)stage.getHeight(), asteroidsNumber, numberOfStars);
            world.setController(controller);

            EventHandler<KeyEvent> pauseEvent = new EventHandler<KeyEvent>() {
                boolean paused = false;

                private PauseMenu pauseMenu = configurePauseMenu();

                private PauseMenu configurePauseMenu() {
                    PauseMenu pauseMenu = new PauseMenu(getGameParent());

                    pauseMenu.setOnResumeAction(e -> {
                        //As if someone pressed P
                        handle(new KeyEvent(null, "", "", P, false, false, false, false));
                    });

                    pauseMenu.setOnRestartAction(e -> {
                        scene.removeEventHandler(KeyEvent.KEY_RELEASED, this);
                        world.stop();
                        humanPlay.fire();
                    });

                    pauseMenu.setOnExitAction(e -> {
                        //TODO: separate this into new method
                        scene.removeEventHandler(KeyEvent.KEY_RELEASED, this);
                        scene.getStylesheets().clear();
                        scene.getStylesheets().add(ClassLoader.getSystemResource(GAME_STYLE_PATH).toExternalForm());

                        controller.disconnect();    //Remove retain cycle
                        Pane root = getGameParent().getRoot();
                        root.getChildren().clear();
                        root.getChildren().add(getGameParent().getPlayMenu());

                        world.stop();
                        getGameParent().getStage().getScene().setRoot(root);
                    });

                    return pauseMenu;
                }

                @Override
                public void handle(KeyEvent event) {
                    KeyCode code = event.getCode();
                    if(code.equals(ESCAPE) || code.equals(P)) {
                        if(paused) {    //Exits out of pause
                            paused = false;

                            getGameParent().hideCursor();

                            Pane pausePane = (Pane) scene.getRoot();
                            Pane game = (Pane) pausePane.getChildren().remove(0);
                            pausePane.getChildren().clear();

                            scene.rootProperty().setValue(game);
                            world.play();
                        } else {
                            paused = true;
                            world.pause();

                            getGameParent().showCursor();

                            StackPane pausePane = new StackPane();
                            pauseMenu.relaod();
                            Pane game = (Pane) scene.getRoot();
                            pausePane.getChildren().addAll(game, pauseMenu);
                            scene.setRoot(pausePane);
                        }
                    }
                }
            };

            scene.addEventHandler(KeyEvent.KEY_RELEASED, pauseEvent);


            world.registerGameOverListener(() -> {
                getGameParent().showCursor();
                scene.removeEventHandler(KeyEvent.KEY_RELEASED, pauseEvent);
                EffectsSoundManager.getInstance().playShipExploded();

                world.pause();
                StackPane pane = new StackPane();
                GameOverScreen gameOverScreen = new GameOverScreen(getGameParent());

                gameOverScreen.setToMenuAction(event -> {
                    world.stop();

                    scene.getStylesheets().clear();
                    scene.getStylesheets().add(ClassLoader.getSystemResource(GAME_STYLE_PATH).toExternalForm());

                    controller.disconnect();    //Remove retain cycle
                    Pane root = getGameParent().getRoot();
                    root.getChildren().clear();
                    root.getChildren().add(getGameParent().getPlayMenu());

                    getGameParent().getStage().getScene().setRoot(root);
                });

                gameOverScreen.setOnRestartAction(event -> {
                    humanPlay.fire();
                });

                pane.getChildren().add(scene.getRoot());

                if(HSDataUtility.getInstance().isHighScore(world.getPoints())) {
                    HighScoreScreen hcs = new HighScoreScreen(world.getPoints(), ScoreElement.HUMAN);
                    hcs.addScoreEnteredListener(() -> {
                        pane.getChildren().remove(hcs);
                        pane.getChildren().add(gameOverScreen);
                    });
                    pane.getChildren().add(hcs);
                } else {
                    pane.getChildren().addAll(gameOverScreen);
                }

                scene.setRoot(pane);
            });

            world.registerFireListener(() -> {
                EffectsSoundManager.getInstance().playFire();
            });

            world.registerExplosionListener(() -> {
                EffectsSoundManager.getInstance().playExplosion();
            });

            world.registerStarCollectedListeners(() -> {
                EffectsSoundManager.getInstance().playStarCollected();
            });


            world.initialize();
            Pane gameSurface = new Pane(world.getGameSurface());
            scene.setRoot(gameSurface);
            scene.getStylesheets().add(ClassLoader.getSystemResource(GAME_WORLD_STYLE_PATH).toExternalForm());
            world.play();
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

    @Override
    public void relaod() {
        //No implementation
    }
}
