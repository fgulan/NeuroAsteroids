package hr.fer.zemris.sm.evolution.representation.FFANN;

import hr.fer.zemris.sm.evolution.representation.IDecoder;
import hr.fer.zemris.sm.evolution.representation.neuralNet.function.IActivationFunction;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;

/**
 * Decoder for {@link DoubleArrayGenotype}.
 * Each time this decoded decodes a genotype it will create new instance of Phenotype class
 *
 * Created by Fredi Šarić on 20.12.15.
 */
public class SteadyStateDecoder implements IDecoder<DoubleArrayGenotype> {

    private int[] layers;
    private IActivationFunction[] activationFunctions;

    public SteadyStateDecoder(int[] layers, IActivationFunction[] activationFunctions) {
        this.layers = layers;
        this.activationFunctions = activationFunctions;
    }

    @Override
    public IPhenotype decode(DoubleArrayGenotype genotype) {
        FFANNGenotypePrototype phenotype = new FFANNGenotypePrototype(layers, activationFunctions);
        phenotype.setGenotype(genotype);
        return phenotype;
    }
}