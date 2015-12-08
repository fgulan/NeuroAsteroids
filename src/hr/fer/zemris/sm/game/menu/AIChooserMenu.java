package hr.fer.zemris.sm.game.menu;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;

import java.text.DecimalFormat;

import static hr.fer.zemris.sm.game.Constants.*;

/**
 *
 * Created by doctor on 03.12.15..
 */
public class AIChooserMenu extends Menu {

    private static DecimalFormat formatter;

    public AIChooserMenu(Game parent) {
        super(parent);
        formatter = new DecimalFormat(FITTNESS_FORMAT);
        setId("chooserMenu");

        SidePane rightPane = new SidePane();
        rightPane.setId(RIGHT_PANE);

        ListView<ListItem> list = new ListView<>();
        fillList(list);
        list.setId(AI_LIST);
        list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ListItem>() {
            @Override //newValue is selected item
            public void changed(ObservableValue<? extends ListItem> observable, ListItem oldValue, ListItem newValue) {
                rightPane.setName(newValue.name);
                rightPane.setComment(newValue.comment);
                rightPane.setFitness(newValue.fitness);
                rightPane.setNeuralNet(newValue.neuralNetwork);
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
        //TODO: fill list @Zmegac
        for(int i = 1; i < 100; i++) {
            list.getItems().add(new ListItem("ListItem " + i, "Abcd",12.4 + i, null));
        }
    }

    private static class ListItem extends BorderPane {
        //TODO: change to phenotype
        Object neuralNetwork;

        String name;
        String comment;
        String fitness;

        public ListItem(String name, String comment, double fitness, Object neuralNetwork) {
            setId(LIST_ITEM);

            this.neuralNetwork = neuralNetwork;
            this.comment = comment;
            this.name = name;
            this.fitness = formatter.format(fitness);

            Label nameLab = new Label("Name: " + name);
            Label fitLab  = new Label("Fitness: " + this.fitness);

            setLeft(nameLab);
            setRight(fitLab);
        }
    }

    private static class SidePane extends VBox {

        private Label name;
        private Label fitness;
        private Label comment;

        private Object neuralNet;

        public SidePane() {
            BorderPane name = createEntry("Name: ", NAME_LABEL, this.name = new Label(), NAME_HBOX_ID);
            BorderPane fitt = createEntry("Fitness: ", FITT_LABEL, this.fitness = new Label(), FITT_HBOX_ID);
            VBox comm = createComment();

            Button play = new KeyEventButton(PLAY_BUTTON_TEXT);
            play.setOnAction(e -> {
                //TODO: place game in this
            });
            VBox labels = new VBox();
            labels.setId(RIGHT_PANE_LABELS);
            labels.getChildren().addAll(name, fitt, comm);
            VBox.setVgrow(labels, Priority.ALWAYS);

            getChildren().addAll(labels, play);
        }

        public void setNeuralNet(Object neuralNet) {
            this.neuralNet = neuralNet;
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
