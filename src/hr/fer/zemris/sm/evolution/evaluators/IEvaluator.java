package hr.fer.zemris.sm.evolution.evaluators;

import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;

import java.util.Collection;

/**
 *
 *
 * Created by Andrija Milicevic.
 */
public interface IEvaluator {

    //TODO: add support for punishment instead of only fitness

    int getInputNodeCount();

    int getOutputNodeCount();

    /**
     * Evaluates given phenotype.
     *
     * After evaluation is over evaluator must give a score to a
     * genotype that produced phenotype that was evaluated.
     * To get the genotype that produced evaluated phenotype
     * use method IPhenotype.getGenotype()
     *
     * @param phenotype phenotype being evaluated
     */
    void evaluate(IPhenotype phenotype);

    /**
     * Evaluates whole population.
     *
     * @param phenotype
     */
    void evaluatePopulation(Collection<IPhenotype> phenotype);
}