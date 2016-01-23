package hr.fer.zemris.sm.evolution.representation;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;

/**
 * Implementation of this interface performs decoding genotype to phenotype.
 *
 * @param <T>
 */
public interface IDecoder<T extends Genotype> {

    IPhenotype decode(T genotype);
}
