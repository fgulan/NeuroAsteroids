package hr.fer.zemris.sm.evolution.termination;

import hr.fer.zemris.sm.evolution.EvolutionaryProcess;

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
