package hr.fer.zemris.sm.evolution.representation.neuralNet.mutation;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.ConnectionGenotype;

/**
 * Disables a random connection from a genotype if active.
 *
 * Created by Andrija Milicevic.
 */
public class DisableConnectionMutation extends Mutation<ConnectionGenotype> {
    public DisableConnectionMutation(double mutationChance) {
        super(mutationChance);
    }

    @Override
    public void mutate(ConnectionGenotype genotype) {
        if (rand.nextDouble() < mutationChance) {
            genotype.getRandomConnection().setActive(false);
        }
    }
}
