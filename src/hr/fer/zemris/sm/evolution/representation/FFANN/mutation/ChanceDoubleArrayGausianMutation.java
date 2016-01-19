package hr.fer.zemris.sm.evolution.representation.FFANN.mutation;

import hr.fer.zemris.sm.evolution.representation.FFANN.DoubleArrayGenotype;
import hr.fer.zemris.sm.evolution.representation.IMutation;

import java.util.Random;

/**
 *
 * Created by Fredi Šarić on 19.12.15.
 */
public class ChanceDoubleArrayGausianMutation implements IMutation<DoubleArrayGenotype> {

    private double sigma;

    private double mutationChance;

    private Random rnd;

    public ChanceDoubleArrayGausianMutation(double sigma, double mutationChance) {
        this.sigma = sigma;
        this.mutationChance = mutationChance;
        this.rnd = new Random();
    }

    @Override
    public void mutate(DoubleArrayGenotype genotype) {
        double[] weights = genotype.getWeights();

        for(int i = 0; i < weights.length; i++) {
            if(mutationChance > rnd.nextDouble()) {
                weights[i] = rnd.nextGaussian() * sigma;
            }
        }

    }
}
