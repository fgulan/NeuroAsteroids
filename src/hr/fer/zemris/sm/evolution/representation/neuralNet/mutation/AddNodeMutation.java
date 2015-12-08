package hr.fer.zemris.sm.evolution.representation.neuralNet.mutation;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.ConnectionGenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.NeuronConnection;

/**
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

        //NeuronConnection connection = genotype.getRandomActiveOutputConnection();

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
