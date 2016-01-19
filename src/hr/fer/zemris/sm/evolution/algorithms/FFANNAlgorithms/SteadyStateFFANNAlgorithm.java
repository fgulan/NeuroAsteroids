package hr.fer.zemris.sm.evolution.algorithms.FFANNAlgorithms;

import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.representation.FFANN.DoubleArrayGenotype;
import hr.fer.zemris.sm.evolution.representation.FFANN.SteadyStateDecoder;
import hr.fer.zemris.sm.evolution.representation.ICrossover;
import hr.fer.zemris.sm.evolution.representation.IMutation;
import hr.fer.zemris.sm.evolution.representation.neuralNet.function.IActivationFunction;
import hr.fer.zemris.sm.evolution.selection.ISelection;

/**
 * This type of algorithm is, because it uses SteadyStateDecoder, will allocate, for every
 * new phenotype it decodes, a new double array with size equal to number of output neurons.
 *
 * This algorithm is implicitly elitist. It will only accept a child into a new population
 * if its better than one of its parent.
 *
 * This algorithm is NOT meant to be used with evaluator that evaluates in multithreading
 * environment, because it needs to evaluate each child before it can create a new child.
 *
 * Created by Fredi Šarić on 19.12.15.
 */
public class SteadyStateFFANNAlgorithm extends AbstractFFANNGeneticAlgorithm {

    public SteadyStateFFANNAlgorithm(int populationSize,
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
    }

    @Override
    public void nextGeneration() {
        for(int i = 0, n = population.size(); i < n;) {     //Create new population
            DoubleArrayGenotype p1 = selection.select(population);
            DoubleArrayGenotype p2 = selection.select(population);

            DoubleArrayGenotype child = crossover.crossover(p1,p2);
            mutation.mutate(child);
            evaluator.evaluate(decoder.decode(child));

            boolean added = removeWorst(p1, p2, child);
            if(added) { //Only if child replaced parent you can increment counter
                i++;
            }
        }
    }

    private boolean removeWorst(DoubleArrayGenotype p1, DoubleArrayGenotype p2, DoubleArrayGenotype child) {
        double childFit = child.getFitness();
        double p1Fit = p1.getFitness();
        double p2Fit = p2.getFitness();

        //If child is better than either parents replace it with worse parent
        if(childFit > p1Fit || childFit > p2Fit) {
            population.add(child);
            if(p1Fit > p2Fit) {
                population.remove(p2);
            } else {
                population.remove(p1);
            }
            return true;
        }
        return false;
    }
}
