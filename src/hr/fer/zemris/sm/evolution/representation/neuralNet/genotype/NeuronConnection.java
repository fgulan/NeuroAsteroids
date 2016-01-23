package hr.fer.zemris.sm.evolution.representation.neuralNet.genotype;

import java.io.Serializable;

/**
 * Represents a NEAT gene which is a neuron connection with its weight, neurons and activity.
 */

public class NeuronConnection implements Comparable<NeuronConnection>, Serializable
{
    private int inNeuron;
    private int outNeuron;
    private double weight;
    private boolean active;

    public NeuronConnection(int inNeuron, int outNeuron, double weight, boolean active) {
        this.inNeuron = inNeuron;
        this.outNeuron = outNeuron;
        this.weight = weight;
        this.active = active;
    }
    public int getInNeuron() {
        return inNeuron;
    }

    public int getOutNeuron() {
        return outNeuron;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int compareTo(NeuronConnection o) {
        if (inNeuron != o.inNeuron) {
            if (inNeuron < o.inNeuron) {
                return -1;
            } else {
                return 1;
            }
        } else {
            if (outNeuron < o.outNeuron) {
                return -1;
            }

            if (outNeuron > o.outNeuron) {
                return 1;
            }
        }

        return 0;
    }

    public NeuronConnection copy() {
        return new NeuronConnection(inNeuron, outNeuron, weight, active);
    }
}