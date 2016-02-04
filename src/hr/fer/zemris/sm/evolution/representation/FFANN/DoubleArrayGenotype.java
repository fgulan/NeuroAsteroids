package hr.fer.zemris.sm.evolution.representation.FFANN;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

/**
 * Genotype representation of fully connected feedforward network.
 *
 * Created by Fredi Šarić on 19.12.15.
 */
public class DoubleArrayGenotype extends Genotype {

    public static final long serialVersionUID = 3978763612070310871L;

    double[] weights;

    public DoubleArrayGenotype(double[] weights) {
        this.weights = weights;
    }

    public double[] getWeights() {
        return weights;
    }

    @Override
    public Genotype copy() {
        double[] copy  = new double[weights.length];
        System.arraycopy(weights, 0, copy, 0, weights.length);

        return new DoubleArrayGenotype(copy);
    }
}
