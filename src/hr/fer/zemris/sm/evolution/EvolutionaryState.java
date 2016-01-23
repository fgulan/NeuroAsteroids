package hr.fer.zemris.sm.evolution;

public enum EvolutionaryState {

	/** Process has not been started yet */
	NOT_STARTED,
	/** Process is running */
	RUNNING,
	/** Processed is paused */
	PAUSED,
	/** Process has been stopped from the outside */
	STOPPED,
	/** Process has finished ( stopping criteria has been met ) */
	FINISHED,

	/**
	 * Epoch is over
 	 */
	EPOCH_OVER,
	/** Epoch has started */
	EPOCH_STARTED
	
}
