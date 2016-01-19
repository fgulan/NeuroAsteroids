package hr.fer.zemris.sm.evolution.representation;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;

public interface IDecoder<T extends Genotype> {

    IPhenotype decode(T genotype);
}
