package hr.fer.zemris.sm.evolution.termination;

import hr.fer.zemris.sm.evolution.EvolutionaryProcess;

import java.io.Serializable;

public interface ITerminationOperator extends Serializable {

	public boolean isFinished(EvolutionaryProcess process);
	
}
