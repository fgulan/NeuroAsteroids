package hr.fer.zemris.sm.evolution.representation.neuralNet.mutation;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.ConnectionGenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.NeuronConnection;

/**
 * Created by Andrija Milicevic.
 */
public class AllWeightGaussianMutation extends Mutation<ConnectionGenotype> {

    private double mutationFactor;

    public AllWeightGaussianMutation(double mutationChance, double mutationFactor) {
        super(mutationChance);
        this.mutationFactor = mutationFactor;
    }

    @Override
    public void mutate(ConnectionGenotype genotype) {

        for (NeuronConnection connection : genotype) {
            if (rand.nextDouble() < mutationChance) {
                connection.setWeight(connection.getWeight() + rand.nextGaussian() * mutationFactor);
            }
        }
    }
}
