package hr.fer.zemris.sm.evolution.termination;

import java.io.Serializable;

import hr.fer.zemris.sm.evolution.IEvolutionaryProcess;

public interface ITerminationOperator extends Serializable {

	public boolean isFinished(IEvolutionaryProcess process);
	
}
