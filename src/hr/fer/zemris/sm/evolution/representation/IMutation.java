package hr.fer.zemris.sm.evolution.representation;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

/**
 * Every implementation of this interface should mutate given genotype.
 *
 * Created by Fredi Šarić on 19.12.15.
 */
public interface IMutation<T extends Genotype> {

    public void mutate(T genotype);

}
