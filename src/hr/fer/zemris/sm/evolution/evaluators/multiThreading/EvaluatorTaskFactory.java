package hr.fer.zemris.sm.evolution.evaluators.multiThreading;

import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;

public interface EvaluatorTaskFactory {

	public EvaluatorTask createTask(IPhenotype phenotype);
	
}
