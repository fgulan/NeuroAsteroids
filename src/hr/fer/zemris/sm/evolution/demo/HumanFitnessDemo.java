package hr.fer.zemris.sm.evolution.demo;

import hr.fer.zemris.sm.evolution.algorithms.FFANNAlgorithms.StopAndEvalAlg;
import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.representation.FFANN.DoubleArrayGenotype;
import hr.fer.zemris.sm.evolution.representation.FFANN.crossover.BLXCrossover;
import hr.fer.zemris.sm.evolution.representation.FFANN.mutation.ChanceDoubleArrayGausianMutation;
import hr.fer.zemris.sm.evolution.representation.ICrossover;
import hr.fer.zemris.sm.evolution.representation.IMutation;
import hr.fer.zemris.sm.evolution.representation.neuralNet.function.IActivationFunction;
import hr.fer.zemris.sm.evolution.representation.neuralNet.function.SigmoidActivationFunction;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.evolution.selection.ISelection;
import hr.fer.zemris.sm.evolution.selection.RouletteWheelSelection;
import hr.fer.zemris.sm.game.Utils.EvolutionObjectDataUtility;
import hr.fer.zemris.sm.game.controllers.FFANNController6;
import hr.fer.zemris.sm.game.nodes.RealNumberInput;
import hr.fer.zemris.sm.game.world.GameWorld;
import hr.fer.zemris.sm.game.world.GraphicsWorld;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Collection;

/**
 * Created by Fredi Šarić on 24.12.15.
 */
public class HumanFitnessDemo extends Application {

    //This changes depending on what type of information you give to the network
    public static final int NETWORK_INPUTS = 4;

    //This is constant
    public static final int NETWORK_OUTPUTS = 4;

    public static final int POPULATION_SIZE = 20;

    //This parameter controls in what range will child be able to get genetic material from parents
    //If p1[i] = -1 and p2[i] = 1 and alpha = 0.5, then child can get gene from interval [-2, 2]
    //This parameter should be in range of [0, 1]
    //The smaller this parameter is them greater impact will crossover have on population in sense
    //that it will collapse population in single point too quickly if mutation is not configured correctly
    public static final double BLX_ALPHA = 0.2;

    //This parameter can be in range from [0, inf]. The way it effects population is that it spreads it.
    //If its too small then the child will mutate very little and it will be very similar to genotype
    //That was produced by crossover. And if crossover is restrictive(BLX_ALPHA is small) than population
    //Will converge too quickly. But if sigma is too big than population will never converge and if you do
    //not have elitists algorithm than you have very high risk of loosing best individual you find, and that
    //individual will be hard to find, because you are searching too big space of solutions.
    public static final double MUTATION_SIGMA = 0.1;

    //This parameter determines what is the chance that child gene will be mutated, again it this is too small
    //Than crossover will take over an collapse population in single point.
    public static final double MUTATION_CHANCE = 1;

    public static final int TOURNAMENT_SELECTION_SIZE = 5;

    public static final int MAXIMAL_ITERATION_OF_ALGORITHM = 1000;

    public static final int SAVE_INTERVAL = 10;

    public static final int[] NETWORK_TOPOLOGY = new int[]{NETWORK_INPUTS, 50, NETWORK_OUTPUTS};

    public static final IActivationFunction[] FUNCTIONS = new IActivationFunction[]{    //num of layers - 1
            new SigmoidActivationFunction(),
            new SigmoidActivationFunction(),
            //new SigmoidActivationFunction()
    };

    public static final int FPS = 120;
    public static final int WIDTH  = 800;
    public static final int HEIGHT = 600;

    public static final int NUM_OF_ASTEROIDS = 5;
    public static final int NUM_OF_STARS = 5;

    private static final double lastBestFitness = 0;

    private Pane worldPane;

    private GameWorld world;

    private HumanEval eval;

    private StopAndEvalAlg alg;

    public IPhenotype currentPhenotype;

    public int evaluationCounter = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        setUpAlg();

        BorderPane pane = new BorderPane();
        VBox sidePane = new VBox();

        RealNumberInput fitnessInput = new RealNumberInput();
        Button evaluateButton = new Button("SetFitness");
        evaluateButton.setOnAction(e -> {
            if(currentPhenotype != null && !fitnessInput.getText().isEmpty()) {
                currentPhenotype.getGenotype().setFitness(Double.parseDouble(fitnessInput.getText()));
            }
        });

        TextField phenotypeName = new TextField();
        phenotypeName.setPromptText("Phenotype name");
        Button savePhenotypeButton = new Button("Save currentPhenotype");
        savePhenotypeButton.setOnAction(e -> {
            if(!phenotypeName.getText().isEmpty()) {
                EvolutionObjectDataUtility.getInstance().saveObject(currentPhenotype,
                        phenotypeName.getText(),
                        currentPhenotype.getGenotype().getFitness(),
                        "NoComment");
                EvolutionObjectDataUtility.getInstance().flush();
            }
        });

        Label iteration = new Label("Iteration 0");
        Label evaluated = new Label("Evaluated 0");
        Label popSize   = new Label("Population size:" + POPULATION_SIZE);

        Button nextPhenotype = new Button("Evaluate next");
        nextPhenotype.setOnAction(e -> {
            alg.evaluateNext();
            iteration.setText("Iteration " + alg.iterCounter);
            evaluated.setText("Evaluated " + (evaluationCounter++ % POPULATION_SIZE));
            startFromNew();
            world.play();
        });

        Button reset = new Button("Reset");
        reset.setOnAction(e -> {
            startFromNew();
            world.play();
        });

        sidePane.getChildren().addAll(  popSize, iteration, evaluated,
                                        fitnessInput, evaluateButton,
                                        phenotypeName, savePhenotypeButton,
                                        nextPhenotype, reset);
        sidePane.setSpacing(15);


        worldPane = new Pane();
        worldPane.setMaxSize(WIDTH + 60, HEIGHT + 60);
        worldPane.setMinSize(WIDTH + 60, HEIGHT + 60);
        pane.setCenter(worldPane);
        pane.setLeft(sidePane);

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setUpAlg() {
        double[][] bounds = createBounds(numOfWeights(), -10, 10);   //Initial bounds of weights

        eval = new HumanEval();

        ICrossover<DoubleArrayGenotype> crossover = new BLXCrossover(BLX_ALPHA);
        IMutation<DoubleArrayGenotype> mutation = new ChanceDoubleArrayGausianMutation(MUTATION_SIGMA, MUTATION_CHANCE);
        ISelection selection = new RouletteWheelSelection();

        alg = new StopAndEvalAlg(POPULATION_SIZE, NETWORK_TOPOLOGY, FUNCTIONS, bounds,
                                 eval, crossover, mutation, selection);

    }

    private void setWorld(GraphicsWorld world) {
        this.world = world;
        worldPane.getChildren().clear();
        worldPane.getChildren().add(world.getGameSurface());
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static int numOfWeights() {
        int sum = 0;

        for (int i = 0; i < NETWORK_TOPOLOGY.length - 1; i++) {
            sum += (NETWORK_TOPOLOGY[i] + 1) * NETWORK_TOPOLOGY[i + 1];
        }

        return sum;
    }

    private static double[][] createBounds(int numOfBounds, double lowerBound, double upperBound) {
        double[][] bounds = new double[numOfBounds][2];
        for (int i = 0; i < numOfBounds; i++) {
            bounds[i][0] = lowerBound;
            bounds[i][1] = upperBound;
        }
        return bounds;
    }

    private void startFromNew() {
        if(this.world != null) {
            this.world.getController().disconnect();
            this.world.setController(null);
            this.world.pause();
        }

        FFANNController6 controller =  new FFANNController6(currentPhenotype);
        GraphicsWorld world = new GraphicsWorld(FPS, WIDTH, HEIGHT, NUM_OF_ASTEROIDS, NUM_OF_STARS);

        world.setController(controller);
        controller.setWorld(world);
        world.initialize();
        setWorld(world);
    }

    private class HumanEval implements IEvaluator {

        @Override
        public int getInputNodeCount() {
            return NETWORK_INPUTS;
        }

        @Override
        public int getOutputNodeCount() {
            return NETWORK_OUTPUTS;
        }

        @Override
        public void evaluate(IPhenotype phenotype) {
            currentPhenotype = phenotype;
        }

        @Override
        public void evaluatePopulation(Collection<IPhenotype> phenotype) {

        }

        @Override
        public double score(IPhenotype phenotype) {
            return 0;
        }
    }
}
