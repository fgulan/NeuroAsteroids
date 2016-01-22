package hr.fer.zemris.sm.game.menu;

import hr.fer.zemris.sm.game.GameConfig;
import hr.fer.zemris.sm.utils.EvolutionElement;
import hr.fer.zemris.sm.utils.EvolutionObjectDataUtility;
import hr.fer.zemris.sm.utils.HSDataUtility;
import hr.fer.zemris.sm.utils.ScoreElement;
import hr.fer.zemris.sm.game.controllers.*;
import hr.fer.zemris.sm.game.menu.menuUtil.KeyEventButton;
import hr.fer.zemris.sm.game.sound.EffectsSoundManager;
import hr.fer.zemris.sm.game.world.GameEvent;
import hr.fer.zemris.sm.game.world.GraphicsWorld;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.List;

import static hr.fer.zemris.sm.game.Constants.*;
import static javafx.scene.input.KeyCode.ESCAPE;
import static javafx.scene.input.KeyCode.P;

/**
 *
 * Created by doctor on 03.12.15..
 */
public class AIChooserMenu extends Menu {

    private static DecimalFormat formatter;

    private ListView<ListItem> list;

    public AIChooserMenu(Game parent) {
        super(parent);
        formatter = new DecimalFormat(FITNESS_FORMAT);
        setId("chooserMenu");

        SidePane rightPane = new SidePane();
        rightPane.setId(RIGHT_PANE);

        list = new ListView<>();
        fillList(list);
        list.setId(AI_LIST);
        list.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                rightPane.setName(newValue.name);
                rightPane.setComment(newValue.comment);
                rightPane.setFitness(newValue.fitness);
            }
        });

        Button back = new KeyEventButton(BACK_BUTTON_TEXT);
        back.setOnAction(e -> parent.transition(this, parent.getPlayMenu(), Game.RIGHT) );

        VBox leftPane = new VBox();
        VBox.setVgrow(list, Priority.ALWAYS);   //Strech list
        leftPane.setId(LEFT_PANE);
        leftPane.getChildren().addAll(list, back);

        setLeft(leftPane);
        setRight(rightPane);
    }

    private void fillList(ListView<ListItem> list) {

        List<EvolutionElement> eObjects = EvolutionObjectDataUtility.getInstance().getNeuralObjects();

        for(EvolutionElement e : eObjects) {
            list.getItems().add(new ListItem(e.getName(), e.getComment(), e.getFitness()));
        }

    }

    private static class ListItem extends BorderPane {

        String name;
        String comment;
        String fitness;

        public ListItem(String name, String comment, double fitness) {
            setId(LIST_ITEM);

            this.comment = comment;
            this.name = name;
            this.fitness = formatter.format(fitness);

            Label nameLab = new Label("Name: " + name);
            Label fitLab  = new Label("Fitness: " + this.fitness);

            setLeft(nameLab);
            setRight(fitLab);
        }
    }

    private class SidePane extends VBox {

        private Label name;
        private Label fitness;
        private Label comment;

        public SidePane() {
            BorderPane name = createEntry("Name: ", NAME_LABEL, this.name = new Label(), NAME_HBOX_ID);
            BorderPane fitt = createEntry("Fitness: ", FITT_LABEL, this.fitness = new Label(), FITT_HBOX_ID);
            VBox comm = createComment();

            Button play = new KeyEventButton(PLAY_BUTTON_TEXT);

            play.setOnAction(e -> {
                Game parent = getGameParent();
                Stage stage = parent.getStage();
                Scene scene = stage.getScene();
                parent.hideCursor();
                IController controller = (IController) EvolutionObjectDataUtility.getInstance().loadObject(this.name.getText());

                int asteroidsNumber = GameConfig.getInstance().getNumberOfComments();
                int starsNumber = GameConfig.getInstance().getNumberOfStars();

                GraphicsWorld world = new GraphicsWorld(60, (int)stage.getWidth(), (int)stage.getHeight(), asteroidsNumber, starsNumber);
                controller.setWorld(world);
                world.setController(controller);
                world.initialize();

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
                            play.fire();
                        });
                        pauseMenu.setOnExitAction(e -> {
                            world.stop();
                            scene.removeEventHandler(KeyEvent.KEY_RELEASED, this);
                            scene.getStylesheets().clear();
                            scene.getStylesheets().add(ClassLoader.getSystemResource(GAME_STYLE_PATH).toExternalForm());

                            Pane root = parent.getRoot();
                            root.getChildren().clear();
                            root.getChildren().add(parent.getAIChooserMenu());

                            scene.setRoot(root);
                        });

                        return pauseMenu;
                    }

                    @Override
                    public void handle(KeyEvent event) {
                        KeyCode code = event.getCode();
                        if(code.equals(ESCAPE) || code.equals(P)) {
                            if(paused) {    //Exits out of pause
                                paused = false;
                                parent.hideCursor();

                                Pane pausePane = (Pane) scene.getRoot();
                                Pane game = (Pane) pausePane.getChildren().remove(0);
                                pausePane.getChildren().clear();

                                scene.rootProperty().setValue(game);
                                world.play();
                            } else {
                                paused = true;
                                parent.showCursor();
                                world.pause();

                                StackPane pausePane = new StackPane();
                                pauseMenu.reload();
                                Pane game = (Pane) scene.getRoot();
                                pausePane.getChildren().addAll(game, pauseMenu);
                                scene.setRoot(pausePane);
                            }
                        }
                    }
                };
                scene.addEventHandler(KeyEvent.KEY_RELEASED, pauseEvent);

                world.addListener(GameEvent.GAME_OVER, gameEvent -> {
                    parent.showCursor();
                    scene.removeEventHandler(KeyEvent.KEY_RELEASED, pauseEvent);
                    EffectsSoundManager.getInstance().playShipExploded();

                    world.pause();
                    StackPane pane = new StackPane();
                    GameOverScreen gameOverScreen = new GameOverScreen(getGameParent());

                    gameOverScreen.setOnRestartAction( event -> {
                        world.stop();
                        play.fire();
                    });

                    gameOverScreen.setToMenuAction( event -> {
                        world.stop();
                        scene.getStylesheets().clear();
                        scene.getStylesheets().add(ClassLoader.getSystemResource(GAME_STYLE_PATH).toExternalForm());

                        Pane root = parent.getRoot();
                        root.getChildren().clear();
                        root.getChildren().add(parent.getAIChooserMenu());

                        scene.setRoot(root);
                    });

                    pane.getChildren().add(scene.getRoot());

                    if(HSDataUtility.getInstance().isHighScore(world.getPoints())) {
                        HighScoreScreen hcs = new HighScoreScreen(world.getPoints(), ScoreElement.AI);
                        hcs.addScoreEnteredListener(() -> {
                            pane.getChildren().remove(hcs);
                            pane.getChildren().add(gameOverScreen);
                        });
                        pane.getChildren().add(hcs);
                    } else {
                        pane.getChildren().add(gameOverScreen);
                    }

                    scene.setRoot(pane);
                });

                world.addListener(GameEvent.MISSILE_FIRED, gameEvent -> EffectsSoundManager.getInstance().playFire());

                world.addListener(GameEvent.ASTEROID_DESTROYED, gameEvent -> EffectsSoundManager.getInstance().playExplosion());

                world.addListener(GameEvent.STAR_COLLECTED, gameEvent -> EffectsSoundManager.getInstance().playStarCollected());

                world.addListener(GameEvent.MISSILE_FIRED, gameEvent -> EffectsSoundManager.getInstance().playFire());

                scene.getStylesheets().add(ClassLoader.getSystemResource(GAME_WORLD_STYLE_PATH).toExternalForm());
                Pane gameSurface = new Pane(world.getGameSurface());
                scene.setRoot(gameSurface);
                world.play();
            });
            VBox labels = new VBox();
            labels.setId(RIGHT_PANE_LABELS);
            labels.getChildren().addAll(name, fitt, comm);
            VBox.setVgrow(labels, Priority.ALWAYS);


            getChildren().addAll(labels, play);
        }

        private BorderPane createEntry(String text, String labelId, Label label, String paneId) {
            Label n = new Label(text);
            label.setId(labelId);
            label.setAlignment(Pos.CENTER_RIGHT);

            BorderPane p = new BorderPane();
            p.setId(paneId);
            p.setLeft(n);
            p.setRight(label);

            return p;
        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public void setFitness(double fitness) {
            this.fitness.setText(formatter.format(fitness));
        }

        public void setFitness(String fitness) {
            this.fitness.setText(formatter.format(Double.parseDouble(fitness)));
        }

        private VBox createComment() {
            Label c = new Label("Comment: ");
            comment = new Label();
            comment.setId(COMM_LABEL);

            VBox box = new VBox();
            box.setId(COMM_VBOX_ID);
            box.getChildren().addAll(c, new Separator(),comment);

            return box;
        }

        private void setComment(String comment) {
            this.comment.setText(comment);
        }
    }

    @Override
    public void reload() {
        list.getItems().clear();
        fillList(list);
    }
}
