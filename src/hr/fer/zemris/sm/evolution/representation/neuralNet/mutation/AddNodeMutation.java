package hr.fer.zemris.sm.evolution.representation.neuralNet.mutation;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.ConnectionGenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.NeuronConnection;

/**
 * Adds a new node to a genotype by modifying an existing connection.
 * The connection is deactivated and replaced with two new connections and a new neuron in between.
 *
 * Created by Andrija Milicevic.
 */
public class AddNodeMutation extends Mutation<ConnectionGenotype>{
    private int maxNodeCount;

    public AddNodeMutation(double mutationChance,int maxNodeCount) {
        super(mutationChance);
        this.maxNodeCount = maxNodeCount;
    }

    @Override
    public void mutate(ConnectionGenotype genotype) {

        if (rand.nextDouble() > mutationChance) {
            return;
        }

        if (genotype.getNeuronCount() >= maxNodeCount) {
            return;
        }

        if (genotype.getConnectionCount() == 0) {
            return;
        }
        NeuronConnection connection = genotype.getRandomConnection();

        if (connection == null) {
            return;
        }

        connection.setActive(false);

        int newNeuron = genotype.addNeuron();

        genotype.addConnection(new NeuronConnection(connection.getInNeuron(), newNeuron, connection.getWeight(), true));
        genotype.addConnection(new NeuronConnection(newNeuron, connection.getOutNeuron(), 1, true));
    }

}
