package hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Helper class used to construct and process NEAT neural networks.
 *
 * Created by Andrija Milicevic.
 */
public class Node implements Serializable {
    public double value;
    public List<NodeConnection> connections;

    public Node() {
        value = 0.0;
        connections = new LinkedList<>();
    }
}