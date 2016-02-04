package hr.fer.zemris.sm.evolution.evaluators.multiThreading;

import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;

/**
 * Factory for creating evaluator tasks for given phenotypes.
 *
 * @param <V>
 */
public interface EvaluatorTaskFactory<V> {

	public EvaluatorTask<V> createTask(IPhenotype phenotype);
	
}
