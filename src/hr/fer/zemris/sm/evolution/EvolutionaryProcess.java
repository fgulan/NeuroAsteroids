package hr.fer.zemris.sm.evolution;

import hr.fer.zemris.sm.evolution.algorithms.IAlgorithm;
import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.termination.ITerminationOperator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

import static hr.fer.zemris.sm.evolution.EvolutionaryState.*;

public class EvolutionaryProcess implements IEvolutionaryProcess {

    private static final long serialVersionUID = 2156460092223481393L;

    private volatile EvolutionaryState state;

    private Map<EvolutionaryState, List<EPListener>> listeners;

    private volatile int iterationCount;

    private IAlgorithm alg;

    private ITerminationOperator termination;

    private IEvaluator evaluator;

    private transient Thread thread;

    private transient EARun algorithm;

    private transient volatile Object lock = new Object();

    public EvolutionaryProcess(IAlgorithm alg,
                               ITerminationOperator term,
                               IEvaluator eval) {
        this.alg = alg;
        this.termination = term;
        this.evaluator = eval;
        state = EvolutionaryState.NOT_STARTED;
    }

    @Override
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

    @Override
    public void pause() {
        algorithm.pause();
    }

    @Override
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

    @Override
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
        //Wait till paused -> TODO: test
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

    public static EvolutionaryProcess loadProcess(Path file) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file, StandardOpenOption.READ));
        EvolutionaryProcess process = (EvolutionaryProcess) in.readObject();
        //process.init = (p) -> {
        //};    //Initialization is already finished
        process.algorithm = process.new EARun();
        return process;
    }

    private void saveToDisk(Path p) throws IOException {
        ObjectOutputStream s = new ObjectOutputStream(Files.newOutputStream(p, StandardOpenOption.CREATE));
        s.writeObject(EvolutionaryProcess.this);
    }

    @Override
    public EvolutionaryState getCurrentState() {
        return state;
    }

    @Override
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

    @Override
    public void removeListener(EPListener listener, EvolutionaryState state) {
        if (listeners != null) {
            List<EPListener> list = listeners.get(state);
            if (list != null) {
                list.remove(listener);
            }
        }
    }

    @Override
    public int getIterationCount() {
        return iterationCount;
    }

    @Override
    public ITerminationOperator getTerminationOperator() {
        return termination;
    }

    @Override
    public void setTerminationOperator(ITerminationOperator op) {
        synchronized (termination) {
            this.termination = op;
        }
    }

    @Override
    public IEvaluator getEvaluator() {
        return this.evaluator;
    }

    @Override
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