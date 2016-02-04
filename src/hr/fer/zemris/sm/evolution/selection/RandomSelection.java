package hr.fer.zemris.sm.evolution.selection;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

import java.util.List;
import java.util.Random;

/**
 * Selects random individual from the population.
 */
public class RandomSelection implements ISelection<Genotype> {

    private static final Random rand = new Random();

    @Override
    public Genotype select(List<Genotype> genotypes) {
        return genotypes.get((int) (rand.nextDouble() * genotypes.size()));
    }
}
