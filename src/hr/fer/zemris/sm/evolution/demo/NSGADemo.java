package hr.fer.zemris.sm.evolution.demo;

import hr.fer.zemris.sm.evolution.EvolutionaryProcess;
import hr.fer.zemris.sm.evolution.EvolutionaryState;
import hr.fer.zemris.sm.evolution.algorithms.FFANNAlgorithms.AbstractFFANNGeneticAlgorithm;
import hr.fer.zemris.sm.evolution.algorithms.FFANNAlgorithms.GenerationalFFANNAlgorithm;
import hr.fer.zemris.sm.evolution.algorithms.IAlgorithm;
import hr.fer.zemris.sm.evolution.evaluators.multiobjective.*;
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
import hr.fer.zemris.sm.evolution.termination.ITerminationOperator;
import hr.fer.zemris.sm.evolution.termination.MaxTerminationCount;
import hr.fer.zemris.sm.utils.EvolutionObjectDataUtility;
import hr.fer.zemris.sm.game.controllers.FFANNController10;

import java.util.List;

/**
 * Demo class for running the NSGA-I algorithm.
 *
 * Created by Fredi Šarić on 15.01.16.
 */
public class NSGADemo {

    private static final int NETWORK_INPUT_NODES = 5;

    private static final int NETWORK_OUTPUT_NODES = 4;

    private static final int POP_SIZE = 100;

    public static final int MAXIMAL_ITERATION_OF_ALGORITHM = 1000;

    //Objective bounds
    private static final double UPPER_BOUND_FOR_STAR_COLLECTED = 20;
    private static final double UPPER_BOUND_FOR_ACCURACY = 10;

    //Share function parameters
    private static final double DISTANCE_THRESHOLD = 2;
    private static final double DISTANCE_ALPHA = 2;

    //Evaluator parameters
    private static final double REDUCING_FACTOR = 0.75; //This is used for reducing maximal fitness of next front
    private static final int NUM_OF_OBJECTIVES = 2;

    //Mutation parameters
    private static final double MUTATION_CHANCE = 0.8;
    private static final double MUTATION_SIGMA  = 0.2;

    //Crossover parameters
    private static final double BLX_ALPHA = 0.2;

    private static final double LOWER_INITIAL_GENOTYPE_BOUND = -1;
    private static final double UPPER_INITIAL_GENOTYPE_BOUND =  1;
    private static final int[] NETWORK_TOPOLOGY = new int[]{NETWORK_INPUT_NODES, 20, NETWORK_OUTPUT_NODES};
    public static final IActivationFunction[] FUNCTIONS = new IActivationFunction[]{    //num of layers - 1
            new SigmoidActivationFunction(),
            new SigmoidActivationFunction(),
    };

    public static void main(String[] args) {
        IDistanceFunction distanceFunction = new ObjectiveSpaceDistanceFunction(createObjectiveBounds());
        ShareFunction shareFunction = new ShareFunction(DISTANCE_THRESHOLD, DISTANCE_ALPHA, distanceFunction);
        MultiObjectiveTaskFactory factory = new MultiObjectiveTaskFactory();


        MultiObjectiveEvaluator evaluator = new MultiObjectiveEvaluator(factory, shareFunction,
                                                           NETWORK_INPUT_NODES, NETWORK_OUTPUT_NODES,
                                                           POP_SIZE, NUM_OF_OBJECTIVES, REDUCING_FACTOR);

        ICrossover<DoubleArrayGenotype> crossover = new BLXCrossover(BLX_ALPHA);
        IMutation<DoubleArrayGenotype> mutation = new ChanceDoubleArrayGausianMutation(MUTATION_SIGMA, MUTATION_CHANCE);

        //Roulette wheel selection is NECESSARY !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        ISelection selection = new RouletteWheelSelection();

        double[][] initialGenotypeBounds = createBounds(numOfWeights(), LOWER_INITIAL_GENOTYPE_BOUND, UPPER_INITIAL_GENOTYPE_BOUND);
        IAlgorithm alg = new GenerationalFFANNAlgorithm(POP_SIZE, NETWORK_TOPOLOGY, FUNCTIONS, initialGenotypeBounds,
                                                        evaluator, crossover, mutation, selection);

        ITerminationOperator term = new MaxTerminationCount(MAXIMAL_ITERATION_OF_ALGORITHM);
        EvolutionaryProcess process = new EvolutionaryProcess(alg,term);

        process.addListener(p -> {
            int iter = p.getIterationCount();
            if(iter % 100 == 0) {
                List<IPhenotype> pop = ((AbstractFFANNGeneticAlgorithm) p.getAlgorithm()).getPhenotypePopulation();

                for(int i = 0; i < pop.size(); i++) {
                    IPhenotype phenotype =  pop.get(i);
                    double fit = phenotype.getGenotype().getFitness();
                    String name = "Iter_" + iter + "_index_" + i + "_fit_" + fit;

                    //TODO: always change the controller you are using
                    EvolutionObjectDataUtility.getInstance().saveObject(new FFANNController10(phenotype), name, fit, "FFANNController10 using NSGA with stars collected and accuracy as objectives");
                }
                EvolutionObjectDataUtility.getInstance().flush();
            }
        }, EvolutionaryState.EPOCH_OVER);

        process.addListener(p -> evaluator.shutDownExecutors(), EvolutionaryState.FINISHED);

        process.start();
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

    private static double[][] createObjectiveBounds() {
        double[][] bounds = new double[NUM_OF_OBJECTIVES][2];
        bounds[0] = new double[]{0, UPPER_BOUND_FOR_STAR_COLLECTED };
        bounds[1] = new double[]{0, UPPER_BOUND_FOR_ACCURACY};
        return bounds;
    }
}
