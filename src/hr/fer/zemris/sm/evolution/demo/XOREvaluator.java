package hr.fer.zemris.sm.evolution.demo;

import hr.fer.zemris.sm.evolution.evaluators.EvaluatorException;
import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;

import java.util.Collection;
import java.util.Random;

public class XOREvaluator implements IEvaluator {

    private static final Random rand = new Random();
    private static final int INPUT_NODE_COUNT = 2;
    private static final int OUTPUT_NODE_COUNT = 1;

    @Override
    public int getInputNodeCount() {
        return INPUT_NODE_COUNT;
    }

    @Override
    public int getOutputNodeCount() {
        return OUTPUT_NODE_COUNT;
    }

    @Override
    public void evaluate(IPhenotype phenotype) throws EvaluatorException {

        double sum = 0;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                double netRes =  phenotype.work(new double[]{i, j})[0];
                double accRes = i ^ j;

                sum += ( netRes - accRes) * ( netRes - accRes) ;
            }
        }

        phenotype.getGenotype().setFitness(4 - sum);
    }

    @Override
    public void evaluatePopulation(Collection<IPhenotype> phenotype) {
        phenotype.forEach(p -> evaluate(p));
    }

    @Override
    public double score(IPhenotype phenotype) {
        int correct = 0;

        for (int i = 0; i < 100; i++) {
            int in1 = rand.nextInt(2);
            int in2 = rand.nextInt(2);

            int netRes = (int) Math.round(phenotype.work(new double[]{in1, in2})[0]);
            int accRes = in1 ^ in2;
            if (netRes == accRes) correct++;
        }

        return correct;
    }
}
