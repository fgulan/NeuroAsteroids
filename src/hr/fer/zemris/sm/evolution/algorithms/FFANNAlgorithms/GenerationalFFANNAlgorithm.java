package hr.fer.zemris.sm.evolution.algorithms.FFANNAlgorithms;

import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.representation.FFANN.DoubleArrayGenotype;
import hr.fer.zemris.sm.evolution.representation.FFANN.FFANNDecoder;
import hr.fer.zemris.sm.evolution.representation.FFANN.SteadyStateDecoder;
import hr.fer.zemris.sm.evolution.representation.ICrossover;
import hr.fer.zemris.sm.evolution.representation.IMutation;
import hr.fer.zemris.sm.evolution.representation.neuralNet.function.IActivationFunction;
import hr.fer.zemris.sm.evolution.selection.ISelection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fredi Šarić on 19.12.15.
 */
public class GenerationalFFANNAlgorithm extends AbstractFFANNGeneticAlgorithm {

    private List<DoubleArrayGenotype> newPopulation;

    private List<DoubleArrayGenotype> tmpPopulation;

    public GenerationalFFANNAlgorithm(int populationSize,
                                      int[] layers,
                                      IActivationFunction[] activationFunctions,
                                      double[][] initPopulationBounds,
                                      IEvaluator evaluator,
                                      ICrossover<DoubleArrayGenotype> crossover,
                                      IMutation<DoubleArrayGenotype> mutation,
                                      ISelection<DoubleArrayGenotype> selection) {
        super(populationSize,
              initPopulationBounds,
              evaluator,
              new SteadyStateDecoder(layers, activationFunctions),
              crossover,
              mutation,
              selection);
        newPopulation = new ArrayList<>(populationSize);
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

        evaluatePop();
    }
}
