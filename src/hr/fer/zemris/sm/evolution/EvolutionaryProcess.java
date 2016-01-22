package hr.fer.zemris.sm.evolution;

import hr.fer.zemris.sm.evolution.algorithms.IAlgorithm;
import hr.fer.zemris.sm.evolution.termination.ITerminationOperator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

import static hr.fer.zemris.sm.evolution.EvolutionaryState.*;

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
public class EvolutionaryProcess implements Serializable {

    private static final long serialVersionUID = 2156460092223481393L;

    private volatile EvolutionaryState state;

    private Map<EvolutionaryState, List<EPListener>> listeners;

    private volatile int iterationCount;

    private IAlgorithm alg;

    private ITerminationOperator termination;

    private transient Thread thread;

    private transient EARun algorithm;

    private final transient Object lock = new Object();

    public EvolutionaryProcess(IAlgorithm alg, ITerminationOperator term) {
        this.alg = alg;
        this.termination = term;
        state = EvolutionaryState.NOT_STARTED;
    }

    /**
     * Starts {@link EvolutionaryProcess}. If process is being
     * started for the first time it will start at <b>Initialize</b> phase, as
     * described in {@link EvolutionaryProcess} life cycle. If
     * process was paused it will start at the start from saved checkpoint.
     *
     * Trying to call <code>start()</code> on a process that is <b>STOPPED</b> or
     * <b>FINISHED</b> state will result in {@link EvolutionaryProcessException}.
     */
    public void start() {
        if (state.equals(EvolutionaryState.STOPPED) || state.equals(EvolutionaryState.FINISHED)) {
            thread = null;    //Will restart process
        }
        if (thread != null) {
            algorithm.resume();
        } else {
            algorithm = new EARun();
            thread = new Thread(algorithm);
            thread.start();
        }
        state = EvolutionaryState.RUNNING;
    }

    /**
     * {@link EvolutionaryProcess} will be paused. Current state will
     * be executed and next in line phase will be saved as checkpoint.
     */
    public void pause() {
        algorithm.pause();
    }

    /**
     * Stops {@link EvolutionaryProcess}. Result of this action are the same as if
     * {@link ITerminationOperator} who always returns <code>true</code>, except
     * if <code>getCurrentState()</code> is called it will return STOPPED
     */
    public void stop() {
        algorithm.stop();
    }

    public void restart() {
        if (!state.equals(STOPPED) && !state.equals(FINISHED)) {
            EPListener l = (p) -> {
                synchronized (lock) {
                    lock.notify();
                }
            };
            addListener(l, STOPPED);
            this.stop();                            //Thread that is running will stop;
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            removeListener(l, STOPPED);
        }
        state = EvolutionaryState.NOT_STARTED;
        thread = null;
        this.start();
    }

    /**
     * Saves {@link EvolutionaryProcess} in given directory with given file name.
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
    //TODO: test this
    public void saveProcces(Path directory, String fileName) throws IOException {
        if (state.equals(FINISHED) || state.equals(STOPPED) || state.equals(FINISHED)) {
            saveToDisk(directory.resolve(fileName));
            return;
        }
        EPListener l = (p) -> {
            synchronized (lock) {
                lock.notify();
            }
        };
        addListener(l, PAUSED);
        pause();
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        removeListener(l, PAUSED);
        saveToDisk(directory.resolve(fileName));
        start(); //Start execution
    }

    /**
     * Returns current states of {@link EvolutionaryProcess}. This operation is
     * thread safe but it does not guarantee that state will remain same after
     * method has returned.
     *
     * @return current states of {@link EvolutionaryProcess}
     */
    //TODO: test this
    public static EvolutionaryProcess loadProcess(Path file) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file, StandardOpenOption.READ));
        EvolutionaryProcess process = (EvolutionaryProcess) in.readObject();
        process.algorithm = process.new EARun();
        return process;
    }

    /**
     * Adds process listener to the listeners that listens to given state.Listeners
     * can be mapped to only states that start with <b>PRE</b> or <b>POST</b> and
     * <b>PAUSED</b>, <b>STARTED</b>, <b>FINISHED</b> and <b>STOPPED</b>. At the start
     * of every state listeners will be notified.
     */
    //TODO: Test this
    private void saveToDisk(Path p) throws IOException {
        ObjectOutputStream s = new ObjectOutputStream(Files.newOutputStream(p, StandardOpenOption.CREATE));
        s.writeObject(EvolutionaryProcess.this);
    }

    public EvolutionaryState getCurrentState() {
        return state;
    }

    /**
     * Adds process listener to the listeners that listens to given state.Listeners
     * can be mapped to only states that start with <b>PRE</b> or <b>POST</b> and
     * <b>PAUSED</b>, <b>STARTED</b>, <b>FINISHED</b> and <b>STOPPED</b>. At the start
     * of every state listeners will be notified.
     */
    public void addListener(EPListener listener, EvolutionaryState state) {
        if (listeners == null) {
            this.listeners = Collections.synchronizedMap(new HashMap<>());
        }
        List<EPListener> list = listeners.get(state);
        if (list == null) {
            list = Collections.synchronizedList(new ArrayList<>());
            listeners.put(state, list);
        }
        list.add(listener);
    }
    /**
     * Removes given listener from set of listeners that are registered for
     * given state. If no such listener exist nothing will happen.
     */
    public void removeListener(EPListener listener, EvolutionaryState state) {
        if (listeners != null) {
            List<EPListener> list = listeners.get(state);
            if (list != null) {
                list.remove(listener);
            }
        }
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public ITerminationOperator getTerminationOperator() {
        return termination;
    }

    public void setTerminationOperator(ITerminationOperator op) {
        synchronized (termination) {
            this.termination = op;
        }
    }

    public IAlgorithm getAlgorithm() {
        return alg;
    }

    private class EARun implements Runnable {

        private volatile boolean stop = false;

        private volatile boolean pause = false;

        private final Object pauseLock = new Object();

        @Override
        public void run() {
            changeState(RUNNING);
            //init.initilze(EvolutionaryProcess.this);

            while (!termination.isFinished(EvolutionaryProcess.this) && !stop) {
                pauseIfAble();
                notifyListeners(EPOH_STARTED);
                alg.nextGeneration();
                iterationCount++;
                notifyListeners(EPOH_OVER);
            }
            if (stop) {
                changeState(STOPPED);
            } else {
                changeState(FINISHED);
            }
        }

        private void changeState(EvolutionaryState state) {
            EvolutionaryProcess.this.state = state;
            notifyListeners(state);
        }

        private void notifyListeners(EvolutionaryState state) {
            List<EPListener> listeners = EvolutionaryProcess.this.listeners.get(state);
            if (listeners != null) {
                for (EPListener l : listeners) {
                    l.listen(EvolutionaryProcess.this);
                }
            }
        }

        void stop() {
            stop = true;
        }

        private void pauseIfAble() {
            if (pause) {
                synchronized (pauseLock) {
                    try {
                        state = PAUSED;
                        notifyListeners(PAUSED);
                        pauseLock.wait();
                    } catch (InterruptedException ignorable) {
                        ignorable.printStackTrace();
                    }
                }
            }
        }

        void pause() {
            pause = true;
        }

        void resume() {
            if (pause) {
                pause = false;
                synchronized (pauseLock) {
                    pauseLock.notify();
                    state = EvolutionaryState.RUNNING;
                }
            }
        }

    }
}