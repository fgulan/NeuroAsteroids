package hr.fer.zemris.sm.evolution.termination;

import hr.fer.zemris.sm.evolution.IEvolutionaryProcess;

public class FittnessTerminationOperator implements ITerminationOperator{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7701292110891868562L;
	private double maxFitness;

	public FittnessTerminationOperator(double maxFitness) {
		super();
		this.maxFitness = maxFitness;
	}

	@Override
	public boolean isFinished(IEvolutionaryProcess process) {
		return false;
		//return process.getPopulation().getBest().getFitness() > maxFitness;
	}
	
	
	
}
