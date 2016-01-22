package hr.fer.zemris.sm.evolution.termination;

import hr.fer.zemris.sm.evolution.EvolutionaryProcess;

public class MaxTerminationCount implements ITerminationOperator{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3431156720842578522L;
	private int maxTermination;

	public MaxTerminationCount(int maxTermination) {
		super();
		if(maxTermination < 1) {
			throw new IllegalArgumentException("Maximal number of iterations must be positive non zero integer!");
		}
		this.maxTermination = maxTermination;
	}

	@Override
	public boolean isFinished(EvolutionaryProcess process) {
		return process.getIterationCount() >= maxTermination;
	}
	
	
	
}
