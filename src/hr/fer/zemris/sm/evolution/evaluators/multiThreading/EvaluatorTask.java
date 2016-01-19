package hr.fer.zemris.sm.evolution.evaluators.multiThreading;

import java.util.concurrent.Callable;

import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;

public abstract class EvaluatorTask<V> implements Callable<V> {

	private IPhenotype phenotype;

	public void setPhenotype(IPhenotype phenotype) {
		this.phenotype = phenotype;
	}

	public IPhenotype getPhenotype() {
		return phenotype;
	}
}
