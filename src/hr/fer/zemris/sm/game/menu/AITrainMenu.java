package hr.fer.zemris.sm.game.menu;

import hr.fer.zemris.sm.game.nodes.IntegerNumberInput;
import hr.fer.zemris.sm.game.nodes.RealNumberInput;
import hr.fer.zemris.sm.game.Constants;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 *
 * Created by doctor on 04.12.15..
 */
public class AITrainMenu extends Menu {

    private NumberAxis x;

    private XYChart.Series<Number, Number> series;

    private int added;

    //***********************************//
    //             Inputs                //
    //***********************************//

    //  General settings
    TextField popSizeInput;

    //  Stopping condition
    TextField maxIterInput;
    TextField maxFitness;
    TextField maxTime;

    public AITrainMenu(Game parent) {
        super(parent);

        Pane centerPane = createCenterPane();
        Pane sidePane   = createSidePane();

        setCenter(centerPane);
        setRight(sidePane);
    }

    private Pane createSidePane() {
        BorderPane pane = new BorderPane();
        pane.setId(Constants.AITRAIN_SIDE_PANE);

        Node inputPane = createInputPane();
        inputPane.setId(Constants.AITRAIN_ACCORDION);

        VBox buttonPane = new VBox();
        buttonPane.setId(Constants.AITRAIN_BUTTON_PANE);
        Button play = new KeyEventButton(Constants.AITRAIN_START_BUTTON);
        Button pause= new KeyEventButton(Constants.AITRAIN_PAUSE_BUTTON);
        Button reset= new KeyEventButton(Constants.AITRAIN_RESET_BUTTON);
        Button save = new KeyEventButton(Constants.AITRAIN_SAVE_BUTTON);
        buttonPane.getChildren().addAll(play, pause, reset, save);

        pane.setCenter(inputPane);
        pane.setBottom(buttonPane);
        return pane;
    }

    private Node createInputPane() {
        Accordion accordion = new Accordion();
        TitledPane generalSettings = new TitledPane("General settings", createGeneralSettings());
        TitledPane stoppingConditions = new TitledPane("Stopping conditions", createStoppingConditionsPane());
        accordion.getPanes().addAll(generalSettings, stoppingConditions);
        return accordion;
    }

    private Node createGeneralSettings() {
        GridPane pane = createGridInputPane();

        Label popSize = new Label(Constants.POPULATION_SIZE_TEXT);
        popSizeInput = new IntegerNumberInput();
        popSizeInput.setPromptText(Constants.POPULATION_PROMPT_TEXT);

        pane.addRow(0, popSize, popSizeInput);
        addSeparator(pane, 1);

        return pane;
    }

    private Node createStoppingConditionsPane() {
        GridPane pane = createGridInputPane();

        Label maxIter = new Label(Constants.MAX_ITER_TEXT);   //Maximal iteration count
        maxIterInput = new IntegerNumberInput();
        maxIterInput.setPromptText(Constants.MAX_ITER_PROMPT);
        pane.addRow(0, maxIter, maxIterInput);

        addSeparator(pane, 1);

        Label maxFit = new Label("Max fitness");   //Maximal fitness
        maxFitness = new RealNumberInput();
        maxFitness.setPromptText("Enter maximal fitness");
        pane.addRow(2, maxFit, maxFitness);

        addSeparator(pane, 4);

        Label maxT = new Label("Max time");
        maxTime = new RealNumberInput();
        maxTime.setPromptText("Enter maximal time (in seconds)");
        pane.addRow(5, maxT, maxTime);

        addSeparator(pane, 6);

        return pane;
    }

    private GridPane createGridInputPane() {
        GridPane pane = new GridPane();

        ColumnConstraints labels = new ColumnConstraints();
        labels.setPercentWidth(50);
        labels.setHalignment(HPos.LEFT);

        ColumnConstraints inputs = new ColumnConstraints();
        inputs.setHalignment(HPos.RIGHT);
        inputs.setPercentWidth(50);
        inputs.setHgrow(Priority.ALWAYS);

        pane.getColumnConstraints().addAll(labels, inputs);

        return pane;
    }

    private void addSeparator(GridPane pane, int row) {
        Separator s = new Separator();
        pane.add(s,0,row,2,1);
    }

    private Pane createCenterPane() {
        Game parent = getGameParent();

        BorderPane centerPane = new BorderPane();
        centerPane.setId(Constants.AITRAIN_CENTER_PANE);
        Node lineChart = createChart();
        lineChart.setId(Constants.EVOLUTION_CHART);

        Button back = new KeyEventButton(Constants.BACK_BUTTON_TEXT);
        back.setId(Constants.AITRAIN_BACK_BUTTON);
        back.setOnAction(e -> parent.transition(this, parent.getStartMenu(), Game.RIGHT));

        centerPane.setCenter(lineChart);
        centerPane.setBottom(back);
        return centerPane;
    }

    private Node createChart() {
        x = new NumberAxis(0, Constants.MAX_GRAPH_X_AXIX, Constants.GRAPH_TICK_SPACE);
        x.setLabel(Constants.X_AXIS_LABLE);
        x.setForceZeroInRange(false);

        NumberAxis y = new NumberAxis();
        y.setLabel(Constants.Y_AXIS_LABLE);

        LineChart<Number, Number> lineChart = new LineChart<>(x,y);
        lineChart.setId(Constants.EVOLUTION_CHART);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(false);

        series = new XYChart.Series<>();
        series.setName(Constants.EVOLUTION_SERIES);
        series.getData().add(new XYChart.Data(0, 10));
        series.getData().add(new XYChart.Data(50, 23));

        added = 12;
        lineChart.getData().add(series);

        return lineChart;
    }
}
