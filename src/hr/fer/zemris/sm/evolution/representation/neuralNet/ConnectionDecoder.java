package hr.fer.zemris.sm.evolution.representation.neuralNet;

import hr.fer.zemris.sm.evolution.representation.IDecoder;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.ConnectionGenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.NeuronConnection;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.ConnectionPhenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.Node;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.NodeConnection;

import java.util.LinkedList;
import java.util.List;

public class ConnectionDecoder implements IDecoder<ConnectionGenotype> {
    private boolean bio[];

    @Override
    public IPhenotype decode(ConnectionGenotype genotype) {
        int nodeCount = genotype.getNeuronCount();

        int inputNodeIndex = genotype.getInputNeuronCount();
        int outputNodeIndex = genotype.getInputNeuronCount() + genotype.getOutputNeuronCount();

        Node[] nodes = new Node[nodeCount];
        List<Integer> evaluationSequence = new LinkedList<>();

        for (int i = 0; i < nodeCount; i++) {
            nodes[i] = new Node();
        }

        for (NeuronConnection connection : genotype) {
            if (connection.isActive()) {
                nodes[connection.getInNeuron()].connections.add(new NodeConnection(connection.getOutNeuron(), connection.getWeight()));
            }
        }

        bio = new boolean[nodeCount];

        for (int i = inputNodeIndex; i < outputNodeIndex; i++) {
            recursiveTrack(i, nodes, evaluationSequence);
        }

        return new ConnectionPhenotype(genotype, nodes, evaluationSequence);
    }

    private void recursiveTrack(int current, Node[] nodes, List<Integer> evaluationSequence) {
        if (bio[current]) {
            return;
        }

        bio[current] = true;

        for (NodeConnection connection : nodes[current].connections) {
            recursiveTrack(connection.outNode, nodes, evaluationSequence);
        }

        evaluationSequence.add(current);
    }


    private int visited[];

    public boolean containsCycle(ConnectionGenotype genotype) {
        int nodeCount = genotype.getNeuronCount();

        int inputNodeIndex = genotype.getInputNeuronCount();
        int outputNodeIndex = genotype.getInputNeuronCount() + genotype.getOutputNeuronCount();

        Node[] nodes = new Node[nodeCount];
        List<Integer> evaluationSequence = new LinkedList<>();

        for (int i = 0; i < nodeCount; i++) {
            nodes[i] = new Node();
        }

        for (NeuronConnection connection : genotype) {
            if (connection.isActive()) {
                nodes[connection.getInNeuron()].connections.add(new NodeConnection(connection.getOutNeuron(), connection.getWeight()));
            }
        }

        visited = new int[nodeCount];

        for (int i = inputNodeIndex; i < outputNodeIndex; i++) {
            if (recursiveCycle(i, nodes, evaluationSequence)) {
                return true;
            }
        }

        return false;
    }

    private boolean recursiveCycle(int i, Node[] nodes, List<Integer> evaluationSequence) {
        if (visited[i] == 1) {
            return true;
        }

        if (visited[i] == 2) {
            return false;
        }

        visited[i] = 1;

        for (NodeConnection connection : nodes[i].connections) {
            if (recursiveCycle(connection.outNode, nodes, evaluationSequence)) {
                return true;
            }
        }

        visited[i] = 2;

        return false;
    }
}