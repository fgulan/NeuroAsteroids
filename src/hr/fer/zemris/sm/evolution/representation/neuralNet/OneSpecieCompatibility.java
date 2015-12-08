package hr.fer.zemris.sm.evolution.representation.neuralNet;

import hr.fer.zemris.sm.evolution.population.Specie;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.ConnectionGenotype;

public class OneSpecieCompatibility implements ISpecieCompatibilityOperator<ConnectionGenotype> {

    @Override
    public boolean isCompatibile(ConnectionGenotype genotype, Specie specie) {
        return true;
    }
}
