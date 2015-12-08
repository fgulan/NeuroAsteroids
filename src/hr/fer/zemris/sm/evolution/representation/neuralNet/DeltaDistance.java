package hr.fer.zemris.sm.evolution.representation.neuralNet;

import hr.fer.zemris.sm.evolution.population.Specie;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.ConnectionGenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.NeuronConnection;

/**
 * Created by Andrija Milicevic.
 */
public class DeltaDistance implements ISpecieCompatibilityOperator<ConnectionGenotype> {

	private final double threshold;
    private final double c1;
    private final double c2;
    private final double c3;
    private final boolean normalize;

    public DeltaDistance(double threshold, double c1, double c2, double c3, boolean normalize) {
        this.threshold = threshold;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.normalize = normalize;
    }

    @Override
    public boolean isCompatibile(ConnectionGenotype genotype1, Specie specie) {
        ConnectionGenotype genotype2 = specie.getSpeciesRepresentative();

        int numberOfGenes;

        if (normalize) {
            numberOfGenes = 1;
        } else  {
            numberOfGenes = Math.max(genotype1.getConnectionCount(), genotype2.getConnectionCount());
        }

        double averageWeightDifference = 0;
        int disjointGenes = 0;
        int sameGenes = 0;

        NeuronConnection testConnection;

        for (NeuronConnection connection : genotype1) {
            testConnection = genotype2.getConnection(connection.getInNeuron(), connection.getOutNeuron());

            if (testConnection == null) {
                disjointGenes++;
            } else {
                sameGenes++;
                averageWeightDifference += Math.abs(connection.getWeight() - testConnection.getWeight());
            }
        }

        for (NeuronConnection connection : genotype2) {
            testConnection = genotype1.getConnection(connection.getInNeuron(), connection.getOutNeuron());

            if (testConnection == null) {
                disjointGenes++;
            }
        }

        averageWeightDifference /= sameGenes;

        double calculatedDistance = c2 * disjointGenes / numberOfGenes;
        calculatedDistance += c3 * averageWeightDifference;

        return (calculatedDistance <= threshold);
    }
}
