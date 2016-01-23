package hr.fer.zemris.sm.evolution.termination;

import hr.fer.zemris.sm.evolution.EvolutionaryProcess;


/**
 * Termination operator that stops execution of algorithm when best individual
 * has fitness greater than given fitness.
 */
/*
	Improvement note to add termination operator for punishment. And to give the ability
	to the algorithm and evaluators to give punishment and fitness.
 */
public class FittnessTerminationOperator implements ITerminationOperator{

	private static final long serialVersionUID = -7701292110891868562L;

	private double maxFitness;

	public FittnessTerminationOperator(double maxFitness) {
		super();
		this.maxFitness = maxFitness;
	}

	@Override
	public boolean isFinished(EvolutionaryProcess process) {
		return process.getAlgorithm().getBestGenotype().getFitness() > maxFitness;
	}
	
	
	
}
