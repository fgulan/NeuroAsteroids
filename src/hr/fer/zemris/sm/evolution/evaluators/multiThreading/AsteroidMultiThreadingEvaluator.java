package hr.fer.zemris.sm.evolution.evaluators.multiThreading;

import hr.fer.zemris.sm.evolution.evaluators.EvaluatorException;
import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.evaluators.multiThreading.EvaluatorTask;
import hr.fer.zemris.sm.evolution.evaluators.multiThreading.EvaluatorTaskFactory;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * Evaluator that evaluates population in multithreading environment.
 * The evaluator tasks should assign fitness for individuals post evaluation.
 */
public final class AsteroidMultiThreadingEvaluator implements IEvaluator {

	private EvaluatorTaskFactory factory;
	
	private ExecutorService executors;

	private int inputNodeCount;

	private int outputNodeCount;

    private int popSize;

    private List<Future<Void>> resultList;

    private int evalCounter = 0;

	public AsteroidMultiThreadingEvaluator(EvaluatorTaskFactory factory, int inputNodeCount, int outputNodeCount, int popSize) {
		this(factory, Runtime.getRuntime().availableProcessors(),inputNodeCount, outputNodeCount, popSize);
	}
	
	public AsteroidMultiThreadingEvaluator(EvaluatorTaskFactory factory, int poolSize, int inputNodeCount, int outputNodeCount, int popSize) {
		this.factory = factory;
		this.executors = Executors.newFixedThreadPool(poolSize);
        this.inputNodeCount = inputNodeCount;
        this.outputNodeCount = outputNodeCount;
        this.popSize = popSize;

        this.resultList = new ArrayList<>(popSize);
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
        resultList.add(executors.submit(task));
        evalCounter++;
        if(evalCounter == popSize) { //When you submit all individual for evaluation than get them
            resultList.forEach(r -> {
                try {
                    r.get();
                } catch (Exception ex) {
                    throw new EvaluatorException("Exception while evaluation", ex);
                }
            });
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

	public void shutDownExecutors() {
		executors.shutdown();
	}
}
