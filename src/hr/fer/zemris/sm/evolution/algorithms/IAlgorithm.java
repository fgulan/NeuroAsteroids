package hr.fer.zemris.sm.evolution.algorithms;

import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

/**
 * Created by Andrija Milicevic.
 */
public interface IAlgorithm {
    void nextGeneration();
    Genotype getBestGenotype();
    IPhenotype getBestPhenotype();
}
