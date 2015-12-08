package hr.fer.zemris.sm.evolution.selection;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

import java.util.List;
import java.util.Random;

/**
 * Created by Andrija Milicevic.
 */
public class TotalRandomTournamentSelection implements ISelection<Genotype> {

    private static final Random rand = new Random();

    private int count;

    public TotalRandomTournamentSelection(int count) {
        this.count = count;
    }

    @Override
    public Genotype select(List<Genotype> genotypes) {
        Genotype best = null;
        Genotype current;

        for (int i = 0; i < count; i++) {
            current = genotypes.get((int) (genotypes.size() * rand.nextDouble()));

            if (best == null || current.getFitness() > best.getFitness()) {
                best = current;
            }
        }

        return best;
    }
}
