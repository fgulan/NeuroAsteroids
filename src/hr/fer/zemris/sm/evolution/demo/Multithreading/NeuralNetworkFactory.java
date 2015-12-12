package hr.fer.zemris.sm.evolution.demo.Multithreading;

import hr.fer.zemris.sm.evolution.evaluators.multiThreading.EvaluatorTask;
import hr.fer.zemris.sm.evolution.evaluators.multiThreading.EvaluatorTaskFactory;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;

/**
 * Created by Fredi Šarić on 12.12.15.
 */
public class NeuralNetworkFactory implements EvaluatorTaskFactory {

    @Override
    public EvaluatorTask createTask(IPhenotype phenotype) {
        return new AsteroidsEvaluatorTask(phenotype);
    }
}
