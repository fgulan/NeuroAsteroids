package hr.fer.zemris.sm.evolution.representation.neuralNet.mutation;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.ConnectionGenotype;

/**
 * Deletes a random connections from a genotype.
 *
 * Created by Andrija Milicevic.
 */
public class DeleteConnectionMutation extends Mutation<ConnectionGenotype> {
    public DeleteConnectionMutation(double mutationChance) {
        super(mutationChance);
    }

    @Override
    public void mutate(ConnectionGenotype genotype) {

        if (rand.nextDouble() < mutationChance) {
            if (genotype.getConnectionCount() == 0) {
                return;
            }
            genotype.removeConnection(genotype.getRandomConnection());
        }
    }
}
