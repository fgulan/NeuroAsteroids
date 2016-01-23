package hr.fer.zemris.sm.evolution.selection;


import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

import java.util.List;

/**
 * Each class that implements this interface should provide some way
 * of selecting individual from population for breading or other actions.
 *
 * @param <T> Type of genotype.
 */
public interface ISelection<T extends Genotype>{

    T select(List<T> genotypes);
}
