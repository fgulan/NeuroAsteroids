package hr.fer.zemris.sm.evolution.evaluators.multiThreading;

public final class MultiThereadingEvaluator  {
/*
	private static final long serialVersionUID = -4708098646658667884L;

	private EvaluatorTaskFactory factory;
	
	private ExecutorService executors;
	
	public MultiThereadingEvaluator(EvaluatorTaskFactory factory, IAdjustedFitnessOperator adjOp) {
		this(factory, Runtime.getRuntime().availableProcessors(),adjOp);
	}
	
	public MultiThereadingEvaluator(EvaluatorTaskFactory factory, int poolSize, IAdjustedFitnessOperator adjOp) {
		super(adjOp);
		this.factory = factory;
		this.executors = Executors.newFixedThreadPool(poolSize);
	}

	@Override
	public void evaluateCurrentState(IEvolutionaryProcess process) {
		IDecoder dec = process.getDecoder();
		List<Callable<Void>> tasks = new ArrayList<>();
		for(Specie s : process.getPopulation()) {
			for(Genotype g : s) {
				tasks.add(factory.createTask(dec.decode(g)));
			}
		}
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
		//Finished all evaluation successfully
	}

	@Override
	public void evaluate(Genotype genotype, IEvolutionaryProcess process) {
		IDecoder decoder = process.getDecoder();
		EvaluatorTask task = factory.createTask(decoder.decode(genotype));
		task.setPhenotype(decoder.decode(genotype));
		Future<Void> result = executors.submit(task);
		try {
			result.get();
		} catch(Exception e) {
			throw new EvaluatorException("Couldn't evaluate genotype.", e);
		}
	}
	*/
}
