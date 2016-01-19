package hr.fer.zemris.sm.evolution.demo.Multithreading;

import hr.fer.zemris.sm.evolution.EvolutionaryProcess;
import hr.fer.zemris.sm.evolution.EvolutionaryState;
import hr.fer.zemris.sm.evolution.algorithms.FFANNAlgorithms.GenerationalFFANNAlgorithm;
import hr.fer.zemris.sm.evolution.algorithms.IAlgorithm;
import hr.fer.zemris.sm.evolution.evaluators.multiThreading.EvaluatorTask;
import hr.fer.zemris.sm.evolution.representation.FFANN.DoubleArrayGenotype;
import hr.fer.zemris.sm.evolution.representation.FFANN.crossover.BLXCrossover;
import hr.fer.zemris.sm.evolution.representation.FFANN.mutation.ChanceDoubleArrayGausianMutation;
import hr.fer.zemris.sm.evolution.representation.ICrossover;
import hr.fer.zemris.sm.evolution.representation.IMutation;
import hr.fer.zemris.sm.evolution.representation.neuralNet.function.IActivationFunction;
import hr.fer.zemris.sm.evolution.representation.neuralNet.function.SigmoidActivationFunction;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.evolution.selection.ElitisticSelection;
import hr.fer.zemris.sm.evolution.selection.ISelection;
import hr.fer.zemris.sm.evolution.selection.RouletteWheelSelection;
import hr.fer.zemris.sm.evolution.selection.TournamentSelection;
import hr.fer.zemris.sm.evolution.termination.ITerminationOperator;
import hr.fer.zemris.sm.evolution.termination.MaxTerminationCount;
import hr.fer.zemris.sm.game.Utils.EvolutionObjectDataUtility;
import hr.fer.zemris.sm.game.controllers.*;
import hr.fer.zemris.sm.game.world.LimitedFramesSimulationWorld;

import java.util.Arrays;

/**
 * Created by Fredi Šarić on 20.12.15.
 */
public class GenerationalDemo {

    //This changes depending on what type of information you give to the network
    public static final int NETWORK_INPUTS = 5;

    //This is constant
    public static final int NETWORK_OUTPUTS = 4;

    public static final int POPULATION_SIZE = 100;

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
    public static final double MUTATION_SIGMA = 0.15;

    //This parameter determines what is the chance that child gene will be mutated, again it this is too small
    //Than crossover will take over an collapse population in single point.
    public static final double MUTATION_CHANCE = 0.8;

    public static final int TOURNAMENT_SELECTION_SIZE = 5;

    public static final int MAXIMAL_ITERATION_OF_ALGORITHM = 1000;

    public static final int[] NETWORK_TOPOLOGY = new int[]{NETWORK_INPUTS, 15, NETWORK_OUTPUTS};

    public static final IActivationFunction[] FUNCTIONS = new IActivationFunction[]{    //num of layers - 1
            new SigmoidActivationFunction(),
            new SigmoidActivationFunction(),
            //new SigmoidActivationFunction()
    };

    private static final double lastBestFitness = 0;

    public static final int SIM_WORLD_WIDTH  = 1600;
    public static final int SIM_WORLD_HEIGHT = 1600;
    public static final int SIM_WORLD_COMET_NUM = 5;
    public static final int SIM_WORLD_STAR_NUM = 1;
    public static final int SIM_WORLD_MAX_FRAMES = 10000;
    public static final int SIM_WORLD_RUNS = 5;

    public static void main(String[] args) {

        double[][] bounds = createBounds(numOfWeights(), -1, 1);   //Initial bounds of weights  double[][] bounds = createBounds(numOfWeights(), -1, 1);   //Initial bounds of weights

        AsteroidMultiThereadingEvaluator eval = new AsteroidMultiThereadingEvaluator(p -> {
            EvaluatorTask task = new AET();
            task.setPhenotype(p);
            return task;
        }, NETWORK_INPUTS, NETWORK_OUTPUTS);

        ICrossover<DoubleArrayGenotype> crossover = new BLXCrossover(BLX_ALPHA);
        IMutation<DoubleArrayGenotype> mutation = new ChanceDoubleArrayGausianMutation(MUTATION_SIGMA, MUTATION_CHANCE);
        ISelection selection = new RouletteWheelSelection();

        IAlgorithm alg = new GenerationalFFANNAlgorithm(POPULATION_SIZE, NETWORK_TOPOLOGY, FUNCTIONS, bounds,
                eval, crossover, mutation, selection);

        ITerminationOperator maximalIterations = new MaxTerminationCount(MAXIMAL_ITERATION_OF_ALGORITHM);

        EvolutionaryProcess process = new EvolutionaryProcess(alg, maximalIterations, eval);

        process.addListener(p -> {  //Prints fitness of the best in population.
            int iter = p.getIterationCount();
            IPhenotype phenotype = p.getAlgorithm().getBestPhenotype();
            double fit = phenotype.getGenotype().getFitness();

            System.out.println("Iteration: " + iter);
            System.out.println("Best fitness: " + fit);

            saveBest(iter, fit, phenotype);
        }, EvolutionaryState.EPOH_STARTED);

        process.addListener(p -> {  //Saves best phenotype in population when algorithm is over.
            IPhenotype phenotype = p.getAlgorithm().getBestPhenotype();
            saveBest(p.getIterationCount(), phenotype.getGenotype().getFitness(), phenotype);

            //Shut down executor service so that process can exit
            eval.shutDownExecutors();
        }, EvolutionaryState.FINISHED);

        process.start();
    }

    private static void saveBest(int iter, double fit, IPhenotype phenotype) {
        String name = "Iter_" + iter + "_fit_" + fit;

        //TODO: always change the controller you are using
        EvolutionObjectDataUtility.getInstance().saveObject(new FFANNController11(phenotype), name, fit, "Generational demo FFANNController11");
        EvolutionObjectDataUtility.getInstance().flush();
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

    private static class AET extends EvaluatorTask {

        @Override
        public Void call() throws Exception {
            FFANNController11 controller = new FFANNController11(getPhenotype());

            double[] res = new double[SIM_WORLD_RUNS];
            for (int i = 0; i < SIM_WORLD_RUNS; i++) {
                LimitedFramesSimulationWorld world = new LimitedFramesSimulationWorld(SIM_WORLD_WIDTH,
                        SIM_WORLD_HEIGHT,
                        SIM_WORLD_COMET_NUM,
                        SIM_WORLD_STAR_NUM,
                        SIM_WORLD_MAX_FRAMES);

                controller.setWorld(world);
                world.setController(controller);
                world.initialize();
                world.play();

                if(world.getFrames() <= 180) {  //Those that do not survive for 3 s will respawn
                    i--;
                    continue;
                }
                /*
                                        DestroyedAsteroids
                       startCollected * ------------------
                                           ShootsFired
                 */
                if(world.getShootsFired() != 0) {
                    double destroyed = (double) world.getAsteroidsDestroyed();
                    res[i] = world.getStarsCollected() + (destroyed * destroyed / world.getShootsFired());
                } else {
                    res[i] = world.getStarsCollected() / 2.0;
                }

                world.setController(null);
            }
            controller.disconnect();

            //Fitness is arithmetic middle of two worst cases
            Arrays.sort(res);
            getPhenotype().getGenotype().setFitness((res[0] + res[1])/2);

            return null;
        }
    }
}
