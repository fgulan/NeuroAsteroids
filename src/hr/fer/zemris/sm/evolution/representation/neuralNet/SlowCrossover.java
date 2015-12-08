package hr.fer.zemris.sm.evolution.representation.neuralNet;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.NeuronConnection;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.ConnectionGenotype;

public class SlowCrossover implements ICrossover<ConnectionGenotype> {

	@Override
    public ConnectionGenotype crossover(ConnectionGenotype parent1, ConnectionGenotype parent2) {

        if (parent1.getFitness() > parent2.getFitness()) {
            ConnectionGenotype swapGenotype = parent1;
            parent1 = parent2;
            parent2 = swapGenotype;
        }

        ConnectionGenotype child = parent1.copy();
        child.setNeuronCount(Math.max(parent1.getNeuronCount(), parent2.getNeuronCount()));

        for (NeuronConnection connection : parent2) {
                child.addConnection(connection.copy());
        }

        return child;
	}
}
