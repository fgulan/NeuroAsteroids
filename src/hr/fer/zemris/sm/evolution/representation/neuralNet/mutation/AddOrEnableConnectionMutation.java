package hr.fer.zemris.sm.evolution.representation.neuralNet.mutation;

import hr.fer.zemris.sm.evolution.representation.neuralNet.ConnectionDecoder;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.ConnectionGenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.NeuronConnection;

/**
 * Selects two random neurons ands adds a new connection between them ( except if they are both input or output neurons ).
 * If a connection already exists between the selected neurons, the connection gets activated instead.
 *
 * Created by Andrija Milicevic.
 */
public class AddOrEnableConnectionMutation extends Mutation<ConnectionGenotype> {
    public AddOrEnableConnectionMutation(double mutationChance) {
        super(mutationChance);
    }
    private static final ConnectionDecoder decoder = new ConnectionDecoder();

    @Override
    public void mutate(ConnectionGenotype genotype) {
        if (mutationChance < rand.nextDouble()) {
            return;
        }
        int neuronCount = genotype.getNeuronCount();
        int neuron1 = (int) (rand.nextDouble() * neuronCount);
        int neuron2 = (int) (rand.nextDouble() * neuronCount);

        if (neuron1 == neuron2) {
            return;
        }

        if (genotype.isInputNeuron(neuron1) && genotype.isInputNeuron(neuron2)) {
            return;
        }

        if (genotype.isOutputNeuron(neuron1) && genotype.isOutputNeuron(neuron2)) {
            return;
        }

        NeuronConnection connection = genotype.getConnection(neuron1, neuron2);

        if (connection != null) {
            connection.setActive(true);
        } else {

            ConnectionGenotype testGenotype = genotype.copy();
            testGenotype.addConnection(new NeuronConnection(neuron1, neuron2, 0.0, true));

            genotype.addConnection(new NeuronConnection(neuron1, neuron2, rand.nextDouble() * 4.0 - 2.0, true));
        }
    }
}
