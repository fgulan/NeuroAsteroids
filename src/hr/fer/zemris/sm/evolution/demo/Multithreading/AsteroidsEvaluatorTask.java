package hr.fer.zemris.sm.evolution.demo.Multithreading;

import hr.fer.zemris.sm.evolution.evaluators.multiThreading.EvaluatorTask;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.game.Constants;
import hr.fer.zemris.sm.game.controllers.NeuralNetworkController;
import hr.fer.zemris.sm.game.world.SimulationWorld;

/**
 * Created by Fredi Šarić on 12.12.15.
 */
public class AsteroidsEvaluatorTask extends EvaluatorTask {

    private IPhenotype phenotype;

    private SimulationWorld world;

    public AsteroidsEvaluatorTask(IPhenotype phenotype) {
        this.phenotype = phenotype;
    }

    @Override
    public Void call() throws Exception {
        //Evaluation
        int res = 0;

        for (int i = 0; i < 5; i++) {
            world = new SimulationWorld(800, 600, Constants.AI_SIMULATION_PLAY_ASTEROIDS_NUMBER, null);
            //THIS LINE is previous implementation if you have better way of making evaluation
            //go ahead an write it
            NeuralNetworkController controller = new NeuralNetworkController(phenotype, world);
            world.setController(controller);
            world.initialize();

            world.play();

            double acc = 0;

            res += controller.fittness + world.getPoints() * 50;
        }

        phenotype.getGenotype().setFitness(res);

        return null;
    }
}
