package hr.fer.zemris.sm.evolution.evaluators.multiobjective;

import hr.fer.zemris.sm.evolution.evaluators.multiThreading.EvaluatorTask;
import hr.fer.zemris.sm.evolution.evaluators.multiThreading.EvaluatorTaskFactory;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;

/**
 * Created by Fredi Šarić on 15.01.16.
 */
public class MultiObjectiveTaskFactory implements EvaluatorTaskFactory<ParetoFrontierIndividual> {

    @Override
    public EvaluatorTask<ParetoFrontierIndividual> createTask(IPhenotype phenotype) {
        MultiObjectiveEvaluatorTask task = new MultiObjectiveEvaluatorTask();
        task.setPhenotype(phenotype);
        return task;
    }
}
