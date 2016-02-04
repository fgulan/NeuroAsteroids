package hr.fer.zemris.sm.evolution.representation.FFANN.mutation;

import hr.fer.zemris.sm.evolution.representation.FFANN.DoubleArrayGenotype;
import hr.fer.zemris.sm.evolution.representation.IMutation;

import java.util.Random;

/**
 * Mutation implementation that operates on {@link DoubleArrayGenotype}.
 * It will perform Gausian mutation on every gene.
 *
 * Created by Fredi Šarić on 19.12.15.
 */
public class DoubleArrayGausianMutation implements IMutation<DoubleArrayGenotype>{

    private Random rnd;

    private double sigma;

    public DoubleArrayGausianMutation(double sigma) {
        this.sigma = sigma;
        this.rnd = new Random();
    }

    @Override
    public void mutate(DoubleArrayGenotype genotype) {
        double[] weights = genotype.getWeights();

        for(int i = 0; i < weights.length; i++) {
            weights[i] += rnd.nextGaussian() * sigma;
        }

    }
}
