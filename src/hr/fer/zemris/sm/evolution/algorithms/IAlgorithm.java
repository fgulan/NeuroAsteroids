package hr.fer.zemris.sm.evolution.algorithms;

import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

/**
 *  Interface for a genetic algorithm used to train neural networks.
 *
 * Created by Andrija Milicevic.
 */
public interface IAlgorithm {

    void nextGeneration();

    Genotype getBestGenotype();

    IPhenotype getBestPhenotype();
}
