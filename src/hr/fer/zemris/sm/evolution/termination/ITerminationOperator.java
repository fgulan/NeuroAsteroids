package hr.fer.zemris.sm.evolution.termination;

import hr.fer.zemris.sm.evolution.EvolutionaryProcess;

import java.io.Serializable;

/**
 * This interface proscribe that some way to check if the process should finish or
 * not depending on process and algorithm state.
 */
public interface ITerminationOperator extends Serializable {

	public boolean isFinished(EvolutionaryProcess process);
	
}
