package hr.fer.zemris.sm.evolution.demo.Multithreading;

import hr.fer.zemris.sm.evolution.evaluators.EvaluatorException;
import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.evaluators.multiThreading.EvaluatorTask;
import hr.fer.zemris.sm.evolution.evaluators.multiThreading.EvaluatorTaskFactory;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class AsteroidMultiThereadingEvaluator implements IEvaluator {

	private static final long serialVersionUID = -4708098646658667884L;

	private EvaluatorTaskFactory factory;
	
	private ExecutorService executors;

	private int inputNodeCount;

	private int outputNodeCount;

	public AsteroidMultiThereadingEvaluator(EvaluatorTaskFactory factory, int inputNodeCount, int outputNodeCount) {
		this(factory, Runtime.getRuntime().availableProcessors(),inputNodeCount, outputNodeCount);
	}
	
	public AsteroidMultiThereadingEvaluator(EvaluatorTaskFactory factory, int poolSize, int inputNodeCount, int outputNodeCount) {
		this.factory = factory;
		this.executors = Executors.newFixedThreadPool(poolSize);
        this.inputNodeCount = inputNodeCount;
        this.outputNodeCount = outputNodeCount;
	}

	@Override
	public int getInputNodeCount() {
		return inputNodeCount;
	}

	@Override
	public int getOutputNodeCount() {
		return outputNodeCount;
	}

	@Override
	public void evaluate(IPhenotype phenotype) {
		EvaluatorTask task = factory.createTask(phenotype);
		Future<Void> result = executors.submit(task);
		try {
			result.get();
		} catch(Exception e) {
			throw new EvaluatorException("Couldn't evaluate genotype.", e);
		}
	}

	@Override
	public void evaluatePopulation(Collection<IPhenotype> phenotypes) {
		List<Callable<Void>> tasks = new ArrayList<>();
		phenotypes.forEach(p -> tasks.add(factory.createTask(p)));

		EvaluatorException exception = null;
		try {
			List<Future<Void>> results = executors.invokeAll(tasks);
			for(Future<Void> f : results) {
				f.get();
			}
		} catch (Exception e) {
			if(exception  == null) {
				exception = new EvaluatorException();
			}
			exception.addSuppressed(e);
		}
		if(exception != null) {
			throw exception;
		}
	}

	@Override
	public double score(IPhenotype phenotype) {
		return 0;
	}

	public void shutDownExecutors() {
		executors.shutdown();
	}
}
