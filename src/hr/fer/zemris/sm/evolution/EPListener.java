package hr.fer.zemris.sm.evolution;

import java.io.Serializable;

/**
 * Implementations of this interface will be notified 
 * when process enters a state that is mapped with 
 * instance of this interface.
 * 
 * @author Fredi Šarić
 *
 */
public interface EPListener extends Serializable {

	/**
	 * Implement this method to "do the action" that is 
	 * required when process enters state mapped to instance 
	 * of this interface.
	 * 
	 * @param process process being listened to
	 */
	public void listen(IEvolutionaryProcess process);
	
}
