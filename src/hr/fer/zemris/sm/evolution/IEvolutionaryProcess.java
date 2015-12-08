package hr.fer.zemris.sm.evolution;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;

import hr.fer.zemris.sm.evolution.algorithms.IAlgorithm;
import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.population.Population;
import hr.fer.zemris.sm.evolution.representation.neuralNet.IDecoder;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.evolution.termination.ITerminationOperator;

/**
 * General evolutionary process specification.
 * <p>
 * When process is created his state is <b>NOT_STARTED</b>, and 
 * can be run with <code>start()</method>. When <code>start()</code>
 * has been called process will start executing process stages in 
 * separate thread, and will be in one or more of the running states or <b>FINISHED</b>
 * state if {@link ITerminationOperator} returned <code>true</code>. 
 * If, while process is in running stages <code>pause()</code> method is 
 * called, process will switch to <b>PAUSED</b> state. From <b>PAUSED</b> state process 
 * can return to running phases by calling <code>start()</code>. To enter 
 * <b>STOPPED</b> state <code>stop()</code> method must be called. Only state from where
 * <b>STOPPED</b> state can not be accessed is <b>FINISHED</b> state. <b>STOPPED</b> and 
 * <b>FINISHED</b> are last state that process can be, and it can not switch to any other 
 * state.
 * <p>
 * Running states of process:
 * <ul>
 * 		<li>Initialization (only passed once)
 * 		<li>Evaluation stage
 * 		<li>Termination stage
 * 		<li>Selection stage
 * 		<li>Evolution stage
 * 			<ul>
 * 				<li>Breeding
 * 				<li>Mutation
 * 			</ul>
 *  </ul>
 *  <br>
 *  <b>Initialization</b> is only passed once and is used to create initial population
 *  and set up evolutionary algorithm parameters.
 *  <br>
 *  <b>Evaluation</b> stage purpose is to evaluate every individual in population that
 *  has not been evaluated.
 *  <br>
 *  <b>Termination</b> stage checks all {@link ITerminationOperator}s stored in process
 *  to check if any of the individuals satisfies termination condition.
 *  <br>
 *  <b>Selection</b> stages removes individuals from population.
 *  <br>
 *  <b>Evolution</b> stage is used for creating new individuals via breeding and mutation
 *  operators. It determines which individuals will be mutated, and which individuals will
 *  be breed together, and by what operators(if multiple operators are set for mutation and/or
 *  breeding). 
 *  <br>
 *  <b>Mutation</b> stage mutates individual with given parameter.
 *  <br>
 *  <b>breeding</b> stage breeds two or more individuals together.
 *  <p>
 *  Because in <b>Evolution</b> stage order of execution is not specified process can be in 
 *  more than one state at any given time.
 *  
 * @author Fredi Šarić
 *
 */
public interface IEvolutionaryProcess extends Serializable {

	
	//TODO: Implements thread management
	
	/**
	 * Starts {@link IEvolutionaryProcess}. If process is being
	 * started for the first time it will start at <b>Initialize</b> phase, as 
	 * described in {@link IEvolutionaryProcess} life cycle. If
	 * process was paused it will start at the start from saved checkpoint.
	 * 
	 * Trying to call <code>start()</code> on a process that is <b>STOPPED</b> or 
	 * <b>FINISHED</b> state will result in {@link EvolutionaryProcessException}.
	 */
	public void start();
	
	/**
	 * {@link IEvolutionaryProcess} will be paused. Current state will
	 * be executed and next in line phase will be saved as checkpoint. 
	 */
	public void pause();

	/**
	 * Stops {@link IEvolutionaryProcess}. Result of this action are the same as if
	 * {@link ITerminationOperator} who always returns <code>true</code>, except 
	 * if <code>getCurrentState()</code> is called it will return {@link EvolutionaryState.STOPPED}.
	 */
	public void stop();
	
	public void restart();
	
	/**
	 * Saves {@link IEvolutionaryProcess} in given directory with given file name.
	 * Once this method is called, <code>pause()</code> will be called and only after
	 * checkpoint is saved, will the process be saved to the file. So when process is
	 * deserialized it will be in PAUSED state.
	 * 
	 * If given directory does not exists or file with given file name exists in 
	 * given directory {@link IOException} will be thrown.
	 * 
	 * @param directory directory where process will be saved 
	 * @param fileName file name under which process will be saved
	 * @throws IOException if given directory does not exist, or if file with given
	 * 					   file name exist
	 */
	public void saveProcces(Path directory, String fileName) throws IOException;
	
	/**
	 * Returns current states of {@link IEvolutionaryProcess}. This operation is
	 * thread safe but it does not guarantee that state will remain same after
	 * method has returned.
	 * 
	 * @return current states of {@link IEvolutionaryProcess}
	 */
	public EvolutionaryState getCurrentState();
	
	/**
	 * Adds process listener to the listeners that listens to given state.Listeners
	 * can be mapped to only states that start with <b>PRE</b> or <b>POST</b> and 
	 * <b>PAUSED</b>, <b>STARTED</b>, <b>FINISHED</b> and <b>STOPPED</b>. At the start 
	 * of every state listeners will be notified. 
	 * 
	 * @param listener listener that listens to given state
	 * @param state state at which listener will be notified
	 */
	public void addListener(EPListener listener, EvolutionaryState state);
	
	/**
	 * Removes given listener from set of listeners that are registered for 
	 * given state. If no such listener exist nothing will happen.
	 * 
	 * @param listener listener that listens to given state
	 * @param state state at which listener will be notified
	 */
	public void removeListener(EPListener listener, EvolutionaryState state);
	
	/**
	 * Returns number of iteration this process has been through. This operation is 
	 * thread safe but it does not guarantee that iteration count will remain same after
	 * method has returned.
	 * 
	 * @return number of {@link IEvolutionaryProcess} iterations
	 */
	public int getIterationCount();
	
	public ITerminationOperator getTerminationOperator();
	
	public void setTerminationOperator(ITerminationOperator op);
	
	public IEvaluator getEvaluator();
	
	public IAlgorithm getAlgorithm();
}
