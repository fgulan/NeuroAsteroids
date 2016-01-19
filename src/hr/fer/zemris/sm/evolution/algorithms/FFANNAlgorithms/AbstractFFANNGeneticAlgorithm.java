package hr.fer.zemris.sm.evolution.algorithms.FFANNAlgorithms;

import hr.fer.zemris.sm.evolution.algorithms.IAlgorithm;
import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.representation.FFANN.DoubleArrayGenotype;
import hr.fer.zemris.sm.evolution.representation.FFANN.FFANNDecoder;
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
import java.util.stream.Collectors;

/**
 *
 * Created by Fredi Šarić on 19.12.15.
 */
public abstract class AbstractFFANNGeneticAlgorithm implements IAlgorithm {

    protected List<DoubleArrayGenotype> population;

    protected IDecoder<DoubleArrayGenotype> decoder;

    protected IMutation<DoubleArrayGenotype> mutation;

    protected ICrossover<DoubleArrayGenotype> crossover;

    protected ISelection<DoubleArrayGenotype> selection;

    protected IEvaluator evaluator;

    protected Random rnd;

    public AbstractFFANNGeneticAlgorithm(int populationSize,
                                         double[][] initPopulationBounds,
                                         IEvaluator evaluator,
                                         IDecoder<DoubleArrayGenotype> decoder,
                                         ICrossover<DoubleArrayGenotype> crossover,
                                         IMutation<DoubleArrayGenotype> mutation,
                                         ISelection<DoubleArrayGenotype> selection) {

        this.population = new ArrayList<>(populationSize);

        this.decoder = decoder;

        this.evaluator = evaluator;

        this.mutation = mutation;
        this.crossover = crossover;
        this.selection = selection;

        this.rnd = new Random();

        initPopulation(rnd, populationSize, initPopulationBounds);
        evaluatePop();
    }

    protected void evaluatePop() {
        evaluator.evaluatePopulation(population.stream().map(g -> decoder.decode(g)).collect(Collectors.toList()));
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
    public abstract void nextGeneration();

    @Override
    public Genotype getBestGenotype() {
        return population.stream().max((o1, o2) -> (int) Math.signum(o1.getFitness() - o2.getFitness())).get();
    }

    @Override
    public IPhenotype getBestPhenotype() {
        return decoder.decode((DoubleArrayGenotype) getBestGenotype());
    }

    public List<IPhenotype> getPhenotypePopulation() {
        return population.stream().map(g -> decoder.decode(g)).collect(Collectors.toList());
    }
}

