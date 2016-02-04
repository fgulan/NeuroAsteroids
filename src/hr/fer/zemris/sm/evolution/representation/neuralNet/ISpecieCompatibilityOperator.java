package hr.fer.zemris.sm.evolution.representation.neuralNet;

import hr.fer.zemris.sm.evolution.population.Specie;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

/**
 * Interface for a specie compatibility measurement.
 * @param <T>
 */
public interface ISpecieCompatibilityOperator<T extends Genotype> {

    boolean isCompatible(T genotype, Specie specie);
}
