package hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype;

import hr.fer.zemris.sm.evolution.representation.neuralNet.function.IActivationFunction;
import hr.fer.zemris.sm.evolution.representation.neuralNet.function.SigmoidActivationFunction;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.ConnectionGenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

import java.util.List;

/**
 * Created by Andrija Milicevic.
 */
public class ConnectionPhenotype implements IPhenotype {

	private static IActivationFunction activationFunction = new SigmoidActivationFunction();

    private ConnectionGenotype genotype;
    private Node[] nodes;
    private List<Integer> evaluationSequence;
    private int nodeCount;

    private int inputNodeIndex;
    private int outputNodeIndex;

    public ConnectionPhenotype(ConnectionGenotype genotype, Node[] nodes, List<Integer> evaluationSequence ) {

        this.genotype = genotype;
        this.nodes = nodes;
        this.evaluationSequence = evaluationSequence;
        this.nodeCount = genotype.getNeuronCount();
        this.inputNodeIndex = genotype.getInputNeuronCount();
        this.outputNodeIndex = genotype.getOutputNeuronCount() + genotype.getInputNeuronCount();
    }

    @Override
    public double[] work(double[] input) {

        for (int current = 0; current < inputNodeIndex; current++) {
            nodes[current].value = input[current];
        }

        // POVRATNE VEZE?
        for (int current = inputNodeIndex; current < nodes.length; current++) {
            nodes[current].value = 0;
        }

        Node currentNode;
        double sum;

        for (int current : evaluationSequence) {
            if (current >= inputNodeIndex) {
                sum = 0;
                currentNode = nodes[current];

                for (NodeConnection connection : currentNode.connections) {
                    sum += nodes[connection.outNode].value * connection.weight;
                }

                nodes[current].value = activationFunction.evaluate(sum);
            }
        }

        double[] output = new double[outputNodeIndex - inputNodeIndex];

        for (int current = inputNodeIndex; current < outputNodeIndex; current++) {
            output[current - inputNodeIndex] = nodes[current].value;
        }

        return output;
    }

    @Override
    public Genotype getGenotype() {
        return genotype;
    }
}
