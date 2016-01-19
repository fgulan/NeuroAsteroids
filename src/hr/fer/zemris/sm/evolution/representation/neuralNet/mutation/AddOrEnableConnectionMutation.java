package hr.fer.zemris.sm.evolution.representation.neuralNet.mutation;

import hr.fer.zemris.sm.evolution.representation.neuralNet.ConnectionDecoder;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.ConnectionGenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.NeuronConnection;

/**
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

        /*
        int tmpNeuron;


        if (neuron1 < neuron2) {
            tmpNeuron = neuron1;
            neuron1 = neuron2;
            neuron2 = tmpNeuron;
        }

        if (genotype.isOutputNeuron(neuron2)) {
            tmpNeuron = neuron1;
            neuron1 = neuron2;
            neuron2 = tmpNeuron;
        }

        if (genotype.isInputNeuron(neuron1)) {
            tmpNeuron = neuron1;
            neuron1 = neuron2;
            neuron2 = tmpNeuron;
        }

*/
        NeuronConnection connection = genotype.getConnection(neuron1, neuron2);

        if (connection != null) {
            connection.setActive(true);
        } else {

            ConnectionGenotype testGenotype = genotype.copy();
            testGenotype.addConnection(new NeuronConnection(neuron1, neuron2, 0.0, true));

            //if (decoder.containsCycle(testGenotype)) {
                //return;
            //}

            genotype.addConnection(new NeuronConnection(neuron1, neuron2, rand.nextDouble() * 4.0 - 2.0, true));
        }
    }
}
