package hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype;

import java.io.Serializable;

/**
 * Helper class used to construct and process NEAT neural networks.
 *
 * Created by Andrija Milicevic.
 */
public class NodeConnection implements Serializable {
    public int outNode;
    public double weight;

    public NodeConnection(int outNode, double weight) {
        this.outNode = outNode;
        this.weight = weight;
    }
}