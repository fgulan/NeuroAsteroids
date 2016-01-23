package hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

import java.io.Serializable;

/**
 * NEAT neural network constructed from a NEAT genotype.
 *
 * @param <T>
 */
public interface IPhenotype<T extends Genotype> extends Serializable {

	double[] work(double[] input);
	
	T getGenotype();
}
