package hr.fer.zemris.sm.evolution.representation.FFANN.crossover;

import hr.fer.zemris.sm.evolution.representation.FFANN.DoubleArrayGenotype;
import hr.fer.zemris.sm.evolution.representation.ICrossover;

import java.util.Random;

/**
 * Created by Fredi Šarić on 19.12.15.
 */
public class RandomChanceCrossover implements ICrossover<DoubleArrayGenotype> {

    private Random rnd;

    @Override
    public DoubleArrayGenotype crossover(DoubleArrayGenotype parent1, DoubleArrayGenotype parent2) {
        double[] p1 = parent1.getWeights();
        double[] p2 = parent2.getWeights();

        double[] child = new double[p1.length];

        for(int i = 0; i < p1.length; i++) {
            if(rnd.nextBoolean()) {
                child[i] = p1[i];
            } else {
                child[i] = p2[i];
            }
        }

        return new DoubleArrayGenotype(child);
    }
}
