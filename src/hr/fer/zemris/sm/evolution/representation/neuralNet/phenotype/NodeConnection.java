package hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype;

/**
 * Created by Andrija Milicevic.
 */
public class NodeConnection {
    public int outNode;
    public double weight;

    public NodeConnection(int outNode, double weight) {
        this.outNode = outNode;
        this.weight = weight;
    }
}