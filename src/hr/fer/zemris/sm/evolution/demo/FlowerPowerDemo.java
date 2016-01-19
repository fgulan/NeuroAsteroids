package hr.fer.zemris.sm.evolution.demo;

import hr.fer.zemris.sm.evolution.EvolutionaryProcess;
import hr.fer.zemris.sm.evolution.EvolutionaryState;
import hr.fer.zemris.sm.evolution.IEvolutionaryProcess;
import hr.fer.zemris.sm.evolution.algorithms.IAlgorithm;
import hr.fer.zemris.sm.evolution.algorithms.SpeciesAlgorithm;
import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.ConnectionGenotype;
import hr.fer.zemris.sm.evolution.termination.ITerminationOperator;
import hr.fer.zemris.sm.evolution.termination.MaxTerminationCount;

import java.io.IOException;

/**
 * Created by Andrija Milicevic.
 */
public class FlowerPowerDemo {

    private static final IEvaluator eval = new FlowerEvaluator("07-iris-formatirano.data");

    public static void main(String[] args) throws IOException {

        IAlgorithm alg = new SpeciesAlgorithm(eval);

        ITerminationOperator term = new MaxTerminationCount(1000);

        IEvolutionaryProcess process = new EvolutionaryProcess(alg,
                term,
                eval);

        process.addListener((p) -> {
            if (p.getIterationCount() % 10 == 0) {
                System.out.println("Iteration " + p.getIterationCount());
                System.out.println("BEST FIT : " + alg.getBestGenotype().getFitness());
                System.out.println("SCORE : " + eval.score(alg.getBestPhenotype()));
            }
        }, EvolutionaryState.EPOH_OVER);
        process.addListener((p) -> evaluateBest(alg.getBestPhenotype()), EvolutionaryState.FINISHED);
        process.start();
    }

    private static void evaluateBest(IPhenotype p) {
        System.out.println(((ConnectionGenotype) p.getGenotype()).getNeuronCount());

        System.out.println(eval.score(p));

        System.out.println(((ConnectionGenotype) p.getGenotype()).getConnectionCount());
    }

}
