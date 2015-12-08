package hr.fer.zemris.sm.evolution.representation.neuralNet.mutation;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

import java.util.Random;

/**
 * Created by Andrija Milicevic.
 */
public abstract class Mutation<T extends Genotype> {
    protected static final Random rand = new Random();
    protected double mutationChance;

    public Mutation(double mutationChance) {
        this.mutationChance = mutationChance;
    }

    public double getMutationChance() {
        return mutationChance;
    }

    public void setMutationChance(double mutationChance) {
        this.mutationChance = mutationChance;
    }

    public abstract void mutate(T genotype);
}
