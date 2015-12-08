package hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

public interface IPhenotype<T extends Genotype> {

	double[] work(double[] input);
	
	T getGenotype();
}
