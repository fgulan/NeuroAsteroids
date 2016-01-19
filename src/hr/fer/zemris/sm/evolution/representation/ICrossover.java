package hr.fer.zemris.sm.evolution.representation;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

public interface ICrossover<T extends Genotype> {

    T crossover(T parent1, T parent2);
}
