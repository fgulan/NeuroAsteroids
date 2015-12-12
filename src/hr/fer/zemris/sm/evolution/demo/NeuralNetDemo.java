package hr.fer.zemris.sm.evolution.demo;

import hr.fer.zemris.sm.evolution.EvolutionaryProcess;
import hr.fer.zemris.sm.evolution.EvolutionaryState;
import hr.fer.zemris.sm.evolution.IEvolutionaryProcess;
import hr.fer.zemris.sm.evolution.algorithms.IAlgorithm;
import hr.fer.zemris.sm.evolution.algorithms.SpeciesAlgorithm;
import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.ConnectionGenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.evolution.termination.ITerminationOperator;
import hr.fer.zemris.sm.evolution.termination.MaxTerminationCount;

import java.util.Random;

public class NeuralNetDemo {

    public static void main(String[] args) {
        IEvaluator eval = new XOREvaluator();

        IAlgorithm alg = new SpeciesAlgorithm(eval);

        ITerminationOperator term = new MaxTerminationCount(500);

        IEvolutionaryProcess process = new EvolutionaryProcess(alg,
                term,
                eval);

        process.addListener((p) -> {
            if (p.getIterationCount() % 10 == 0) {
                System.out.println("Iteration " + p.getIterationCount());
                System.out.println(alg.getBestGenotype().getFitness());
                System.out.println("BEST FIT : " + alg.getBestGenotype().getFitness());
                System.out.println("SCORE : " + eval.score(alg.getBestPhenotype()));
            }
        }, EvolutionaryState.EPOH_OVER);
        process.addListener((p) -> {
            IPhenotype best = alg.getBestPhenotype();

            for(int i = 0;i < 5; i++) {
                eval.evaluate(alg.getBestPhenotype());
                System.out.println(best.getGenotype().getFitness());
            }

            evaluateBest(alg.getBestPhenotype());
        }, EvolutionaryState.FINISHED);
        process.start();
    }

    private static void evaluateBest(IPhenotype p) {
        System.out.println(((ConnectionGenotype) p.getGenotype()).getNeuronCount());
        Random rnd = new Random();
        int correct = 0;
        long i1 = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            int in1 = rnd.nextInt(2);
            int in2 = rnd.nextInt(2);

            int netRes = (int) Math.round(p.work(new double[]{in1, in2})[0]);
            int accRes = in1 ^ in2;
            if (netRes == accRes) correct++;
        }
        long i2 = System.currentTimeMillis();
        System.out.println("Correct percent: " + correct + "%");
        System.out.println("Evaluation time: " + (i2 - i1));

        double sum = 0;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                double netRes = p.work(new double[]{i, j})[0];
                double accRes = i ^ j;


                System.out.println(i + " " + j + " " + netRes);
                sum += (netRes - accRes) * (netRes - accRes);


            }
        }
        System.out.println(4 - sum);
        System.out.println(((ConnectionGenotype) p.getGenotype()).getConnectionCount());
    }
}
