package hr.fer.zemris.sm.evolution.representation.neuralNet;

import hr.fer.zemris.sm.evolution.population.Specie;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.ConnectionGenotype;

/**
 * Removes the speciation in the NEAT algorithm.
 */
public class OneSpecieCompatibility implements ISpecieCompatibilityOperator<ConnectionGenotype> {

    @Override
    public boolean isCompatible(ConnectionGenotype genotype, Specie specie) {
        return true;
    }
}
