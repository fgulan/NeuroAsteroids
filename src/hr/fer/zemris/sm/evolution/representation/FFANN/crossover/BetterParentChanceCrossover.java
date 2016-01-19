package hr.fer.zemris.sm.evolution.representation.FFANN.crossover;

import hr.fer.zemris.sm.evolution.representation.FFANN.DoubleArrayGenotype;
import hr.fer.zemris.sm.evolution.representation.ICrossover;

import java.util.Random;

/**
 * Created by Fredi Šarić on 19.12.15.
 */
public class BetterParentChanceCrossover implements ICrossover<DoubleArrayGenotype> {

    private double crossoverChance;

    private Random rnd;

    public BetterParentChanceCrossover(double crossoverChance) {
        this.crossoverChance = crossoverChance;
    }

    @Override
    public DoubleArrayGenotype crossover(DoubleArrayGenotype parent1, DoubleArrayGenotype parent2) {
        double[] betterParent = parent1.getFitness() > parent2.getFitness() ? parent1.getWeights() : parent2.getWeights();
        double[] worseParent  = parent1.getFitness()<= parent2.getFitness() ? parent1.getWeights() : parent2.getWeights();

        double[] child = new double[parent1.getWeights().length];

        for(int i = 0; i < child.length; i++) {

            if(crossoverChance > rnd.nextDouble()) {
                child[i] = betterParent[i];
            } else {
                child[i] = worseParent[i];
            }
        }

        return new DoubleArrayGenotype(child);
    }
}
