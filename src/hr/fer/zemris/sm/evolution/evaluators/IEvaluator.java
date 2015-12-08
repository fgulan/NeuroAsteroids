package hr.fer.zemris.sm.evolution.evaluators;

import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;

/**
 * Created by Andrija Milicevic.
 */
public interface IEvaluator {
    int getInputNodeCount();
    int getOutputNodeCount();
    double evaluate(IPhenotype phenotype);

    double score(IPhenotype phenotype);
}