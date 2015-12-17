package hr.fer.zemris.sm.evolution.representation.neuralNet.genotype;

import java.io.Serializable;

public abstract class Genotype implements Comparable<Genotype>, Iterable<NeuronConnection>, Serializable {
    private double fitness;

    @Override
    public int compareTo(Genotype o) {
        if (fitness < o.fitness) {
            return 1;
        }

        if (fitness > o.fitness) {
            return -1;
        }

        return 0;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public abstract Genotype copy();
}