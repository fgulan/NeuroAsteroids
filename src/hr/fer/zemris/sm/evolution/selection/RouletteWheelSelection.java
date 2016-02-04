package hr.fer.zemris.sm.evolution.selection;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

import java.util.List;
import java.util.Random;

/**
 * Selects individual from the population from proportionally based on fitness.
 * All fitness should be positive number.
 */
public class RouletteWheelSelection implements ISelection<Genotype> {

    private static final Random rand = new Random();

    @Override
    public Genotype select(List<Genotype> genotypes) {
        double sum = genotypes.stream().mapToDouble(Genotype::getFitness).sum();
        double rndVal = rand.nextDouble();
        double currSum = 0;
        for (Genotype b : genotypes) {
            currSum += b.getFitness();
            if ((currSum / sum) >= rndVal) {
                return b;
            }
        }

        //Will not happen
        return genotypes.get(genotypes.size() - 1);
    }
}
