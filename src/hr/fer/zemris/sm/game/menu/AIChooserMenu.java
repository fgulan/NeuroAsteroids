package hr.fer.zemris.sm.game.menu;

import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.game.Utils.EvolutionElement;
import hr.fer.zemris.sm.game.Utils.EvolutionObjectDataUtility;
import hr.fer.zemris.sm.game.controllers.NeuralNetworkController;
import hr.fer.zemris.sm.game.sound.EffectsSoundManager;
import hr.fer.zemris.sm.game.world.GraphicsWorld;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.List;

import static hr.fer.zemris.sm.game.Constants.*;

/**
 *
 * Created by doctor on 03.12.15..
 */
public class AIChooserMenu extends Menu {

    private static DecimalFormat formatter;

    private ListView<ListItem> list;

    public AIChooserMenu(Game parent) {
        super(parent);
        formatter = new DecimalFormat(FITTNESS_FORMAT);
        setId("chooserMenu");

        SidePane rightPane = new SidePane();
        rightPane.setId(RIGHT_PANE);

        list = new ListView<>();
        fillList(list);
        list.setId(AI_LIST);
        list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ListItem>() {
            @Override //newValue is selected item
            public void changed(ObservableValue<? extends ListItem> observable, ListItem oldValue, ListItem newValue) {
                System.out.println(newValue.name + " " + newValue.comment + " " + newValue.fitness);
                rightPane.setName(newValue.name);
                rightPane.setComment(newValue.comment);
                rightPane.setFitness(newValue.fitness);
            }
        });


        Button back = new KeyEventButton(BACK_BUTTON_TEXT);
        back.setOnAction(e -> {
            parent.transition(this, parent.getPlayMenu(), Game.RIGHT);
        });

        VBox leftPane = new VBox();
        VBox.setVgrow(list, Priority.ALWAYS);   //Strech list
        leftPane.setId(LEFT_PANE);
        leftPane.getChildren().addAll(list, back);

        setLeft(leftPane);
        setRight(rightPane);
    }

    private void fillList(ListView<ListItem> list) {

        List<EvolutionElement> eObjects = EvolutionObjectDataUtility.getInstance().getNeuralObjects();
        System.out.println(eObjects.size());


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
                IPhenotype network = (IPhenotype) EvolutionObjectDataUtility.getInstance().loadObject(this.name.getText());

                GraphicsWorld world = new GraphicsWorld(60, (int)stage.getWidth(), (int)stage.getHeight(), 25, null);
                NeuralNetworkController controller = new NeuralNetworkController(network, world);
                world.setController(controller);
                world.initialize();

                world.registerGameOverListener(() -> {
                    EffectsSoundManager.getInstance().playShipExploded();

                    world.pause();
                    StackPane pane = new StackPane();
                    pane.getChildren().addAll(scene.getRoot(), new GameOverScreen(getGameParent()));
                    scene.setRoot(pane);
                });

                world.registerFireListener(() -> {
                    EffectsSoundManager.getInstance().playFire();
                });

                world.registerExplosionListener(() -> {
                    EffectsSoundManager.getInstance().playExplosion();
                });
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
}
