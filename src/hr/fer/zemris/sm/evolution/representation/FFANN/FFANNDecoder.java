package hr.fer.zemris.sm.evolution.representation.FFANN;

import hr.fer.zemris.sm.evolution.representation.IDecoder;
import hr.fer.zemris.sm.evolution.representation.neuralNet.function.IActivationFunction;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;

/**
 *
 * This type of phenotype decoder will only work if you decode
 * whole population every iteration, otherwise you may loose genotypes.
 * Or mix them with previous population.
 *
 * Created by Fredi Šarić on 19.12.15.
 */
public class FFANNDecoder implements IDecoder<DoubleArrayGenotype> {

    private FFANNGenotypePrototype[] prototypes;

    private int decodedCounter;

    public FFANNDecoder(int populationSize, int[] layers, IActivationFunction[] activationFunctions) {
        this.prototypes = new FFANNGenotypePrototype[populationSize];

        for(int i = 0; i < populationSize; i++) {
            prototypes[i] = new FFANNGenotypePrototype(layers, activationFunctions);
        }

        decodedCounter = 0;
    }

    /**
     * It will reuse all prototypes to produce genotypes.
     * @param genotype
     * @return
     */
    @Override
    public IPhenotype decode(DoubleArrayGenotype genotype) {
        prototypes[decodedCounter % prototypes.length].setGenotype(genotype);
        return prototypes[decodedCounter++ % prototypes.length];
    }
}
