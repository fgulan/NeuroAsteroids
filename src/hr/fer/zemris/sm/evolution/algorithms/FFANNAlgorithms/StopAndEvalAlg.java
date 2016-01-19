package hr.fer.zemris.sm.evolution.algorithms.FFANNAlgorithms;

import hr.fer.zemris.sm.evolution.algorithms.IAlgorithm;
import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.representation.FFANN.DoubleArrayGenotype;
import hr.fer.zemris.sm.evolution.representation.FFANN.SteadyStateDecoder;
import hr.fer.zemris.sm.evolution.representation.ICrossover;
import hr.fer.zemris.sm.evolution.representation.IDecoder;
import hr.fer.zemris.sm.evolution.representation.IMutation;
import hr.fer.zemris.sm.evolution.representation.neuralNet.function.IActivationFunction;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.evolution.selection.ISelection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Fredi Šarić on 24.12.15.
 */
public class StopAndEvalAlg implements IAlgorithm {

    protected List<DoubleArrayGenotype> population;

    protected IDecoder<DoubleArrayGenotype> decoder;

    protected IMutation<DoubleArrayGenotype> mutation;

    protected ICrossover<DoubleArrayGenotype> crossover;

    protected ISelection<DoubleArrayGenotype> selection;

    protected IEvaluator evaluator;

    protected Random rnd;

    private List<DoubleArrayGenotype> newPopulation;

    private List<DoubleArrayGenotype> tmpPopulation;

    private int evalCounter = 0;

    public int iterCounter;

    private List<GenListeners> listeners;

    public StopAndEvalAlg(int populationSize,
                                      int[] layers,
                                      IActivationFunction[] activationFunctions,
                                      double[][] initPopulationBounds,
                                      IEvaluator evaluator,
                                      ICrossover<DoubleArrayGenotype> crossover,
                                      IMutation<DoubleArrayGenotype> mutation,
                                      ISelection<DoubleArrayGenotype> selection) {
        this.population = new ArrayList<>(populationSize);

        this.decoder = new SteadyStateDecoder(layers, activationFunctions);

        this.evaluator = evaluator;

        this.mutation = mutation;
        this.crossover = crossover;
        this.selection = selection;

        this.listeners = new ArrayList<>();

        this.rnd = new Random();

        initPopulation(rnd, populationSize, initPopulationBounds);
        newPopulation = new ArrayList<>(populationSize);
    }

    private void initPopulation(Random rnd, int populationSize, double[][] initPopulationBounds) {
        for(int i  = 0; i < populationSize; i++) {
            double[] weights = new double[initPopulationBounds.length];
            for(int j = 0; j < initPopulationBounds.length; j ++) {
                double min = initPopulationBounds[j][0];
                double max = initPopulationBounds[j][1];

                weights[j] = rnd.nextDouble() * (max - min) - min;  //TODO: check this
            }
            population.add(new DoubleArrayGenotype(weights));
        }
    }

    @Override
    public void nextGeneration() {
        newPopulation.clear();  //Make room for new individuals

        for(int i = 0, n = population.size() - 1; i < n; i++) {     //Create new population
            DoubleArrayGenotype p1 = selection.select(population);
            DoubleArrayGenotype p2 = selection.select(population);

            DoubleArrayGenotype child = crossover.crossover(p1,p2);
            mutation.mutate(child);

            newPopulation.add(child);
        }

        //Store best so far genotype
        newPopulation.add((DoubleArrayGenotype) getBestGenotype());

        //change references(this is faster than clearing population and adding from new population)
        tmpPopulation = population;
        population = newPopulation;
        newPopulation = tmpPopulation;
    }

    public void evaluateNext() {
        if(evalCounter == population.size()) {
            iterCounter++;
            nextGeneration();
            evalCounter=0;
        }
        evaluator.evaluate(decoder.decode(population.get(evalCounter)));
        evalCounter++;
    }

    @Override
    public Genotype getBestGenotype() {
        return population.stream().max((o1, o2) -> (int) Math.signum(o1.getFitness() - o2.getFitness())).get();
    }

    @Override
    public IPhenotype getBestPhenotype() {
        return decoder.decode((DoubleArrayGenotype) getBestGenotype());
    }

    public static interface GenListeners {
        public void genOver();
    }
}
