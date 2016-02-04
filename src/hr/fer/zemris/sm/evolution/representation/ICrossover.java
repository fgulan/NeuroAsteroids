package hr.fer.zemris.sm.evolution.representation;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

/**
 * Every implementation of this interface should take two parents and construct new instance
 * of the same class, from parents genes.
 *
 * @param <T> Type of genotype
 */
public interface ICrossover<T extends Genotype> {

    T crossover(T parent1, T parent2);
}
