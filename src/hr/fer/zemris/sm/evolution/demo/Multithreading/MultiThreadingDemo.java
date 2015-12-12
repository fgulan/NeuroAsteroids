package hr.fer.zemris.sm.evolution.demo.Multithreading;

import hr.fer.zemris.sm.evolution.EvolutionaryProcess;
import hr.fer.zemris.sm.evolution.EvolutionaryState;
import hr.fer.zemris.sm.evolution.IEvolutionaryProcess;
import hr.fer.zemris.sm.evolution.algorithms.IAlgorithm;
import hr.fer.zemris.sm.evolution.algorithms.SpeciesAlgorithm;
import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.ConnectionGenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.evolution.termination.ITerminationOperator;
import hr.fer.zemris.sm.evolution.termination.MaxTerminationCount;

import java.io.IOException;

/**
 * Created by Fredi Šarić on 12.12.15.
 */
public class MultiThreadingDemo {

    public static void main(String[] args) throws IOException {
        //Neural net with 1 input and 4 outputs
        IEvaluator eval = new AsteroidMultiThereadingEvaluator(new NeuralNetworkFactory(), 1, 4);

        IAlgorithm alg = new SpeciesAlgorithm(eval);

        ITerminationOperator term = new MaxTerminationCount(1000);

        IEvolutionaryProcess process = new EvolutionaryProcess(alg,
                term,
                eval);

        process.addListener((p) -> {
            if (p.getIterationCount() % 1 == 0) {
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

        System.out.println(((ConnectionGenotype) p.getGenotype()).getConnectionCount());
    }


}
