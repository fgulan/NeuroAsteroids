package hr.fer.zemris.sm.evolution.evaluators.multiThreading;

import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;

public interface EvaluatorTaskFactory<V> {

	public EvaluatorTask<V> createTask(IPhenotype phenotype);
	
}
