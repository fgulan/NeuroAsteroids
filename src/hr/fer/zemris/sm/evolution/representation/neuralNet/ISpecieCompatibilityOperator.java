package hr.fer.zemris.sm.evolution.representation.neuralNet;

import hr.fer.zemris.sm.evolution.population.Specie;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

public interface ISpecieCompatibilityOperator<T extends Genotype> {

    boolean isCompatibile(T genotype, Specie specie);
}
