package hr.fer.zemris.sm.evolution.demo;

import hr.fer.zemris.sm.evolution.EvolutionaryProcess;
import hr.fer.zemris.sm.evolution.EvolutionaryState;
import hr.fer.zemris.sm.evolution.algorithms.IAlgorithm;
import hr.fer.zemris.sm.evolution.algorithms.SpeciesAlgorithm;
import hr.fer.zemris.sm.evolution.evaluators.AsteroidsEvaluator;
import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.ConnectionGenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.evolution.termination.ITerminationOperator;
import hr.fer.zemris.sm.evolution.termination.MaxTerminationCount;

import java.io.IOException;

/**
 * Main method which runs the NEAT algorithm for the Asteroids game.
 *
 * Created by Andrija Milicevic.
 */
public class AsteroidsDemo {
    private static final IEvaluator eval = new AsteroidsEvaluator();

    public static void main(String[] args) throws IOException {

        IAlgorithm alg = new SpeciesAlgorithm(eval);

        ITerminationOperator term = new MaxTerminationCount(1000);

        EvolutionaryProcess process = new EvolutionaryProcess(alg, term);

        process.addListener((p) -> {
            if (p.getIterationCount() % 10 == 0) {
                System.out.println("Iteration " + p.getIterationCount());
                System.out.println("BEST FIT : " + alg.getBestGenotype().getFitness());
            }
        }, EvolutionaryState.EPOCH_OVER);
        process.addListener((p) -> evaluateBest(alg.getBestPhenotype()), EvolutionaryState.FINISHED);
        process.start();
    }

    private static void evaluateBest(IPhenotype p) {
        System.out.println(((ConnectionGenotype) p.getGenotype()).getNeuronCount());

        System.out.println(((ConnectionGenotype) p.getGenotype()).getConnectionCount());
    }

}

