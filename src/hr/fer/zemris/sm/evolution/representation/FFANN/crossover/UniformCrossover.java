package hr.fer.zemris.sm.evolution.representation.FFANN.crossover;

import hr.fer.zemris.sm.evolution.representation.FFANN.DoubleArrayGenotype;
import hr.fer.zemris.sm.evolution.representation.ICrossover;

/**
 * Crossover implementation that will take one gene from one parent, and
 * next gene for other parent. That process is repeated to the end of the genotype.
 *
 * Created by Fredi Šarić on 19.12.15.
 */
public class UniformCrossover implements ICrossover<DoubleArrayGenotype> {


    @Override
    public DoubleArrayGenotype crossover(DoubleArrayGenotype parent1, DoubleArrayGenotype parent2) {
        double[] p1 = parent1.getWeights();
        double[] p2 = parent2.getWeights();

        double[] child = new double[p1.length];

        for(int i = 0; i < p1.length; i++) {
            if(i%2 == 0) {
                child[i] = p1[i];
            } else {
                child[i] = p2[i];
            }
        }

        return new DoubleArrayGenotype(child);
    }
}
