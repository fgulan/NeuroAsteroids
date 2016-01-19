package hr.fer.zemris.sm.evolution.demo;

import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.game.controllers.NeuralNetworkController;
import hr.fer.zemris.sm.game.world.SimulationWorld;
import hr.fer.zemris.sm.game.world.listeners.FireListener;
import static hr.fer.zemris.sm.game.Constants.*;

import java.util.Collection;

/**
 * Created by Andrija Milicevic.
 */
public class AsteroidesEvaluator implements IEvaluator {

    SimulationWorld world;

    @Override
    public int getInputNodeCount() {
        return 1;
    }

    @Override
    public int getOutputNodeCount() {
        return 4;
    }

    @Override
    public void evaluate(IPhenotype phenotype) {
        int res = 0;

        for (int i = 0; i < 5; i++) {
            world = new SimulationWorld(800, 600, AI_SIMULATION_PLAY_ASTEROIDS_NUMBER, STARS_NUMBER);
            NeuralNetworkController controller = new NeuralNetworkController(phenotype, world);
            world.setController(controller);
            world.initialize();

            FireCounter fc = new FireCounter();
            world.registerFireListener(fc);
            world.play();

            double acc = 0;
            if (fc.count != 0) {
                acc = world.getPoints() / (double) fc.count;
            }

            res += controller.fittness + world.getPoints() * 50;
        }
        //System.out.println("acc:" + acc + " fcCount" + fc.count + " destroyed" + world.getPoints() + " fitness " + controller.fittness);

        //System.out.println(res / 5.0);

        phenotype.getGenotype().setFitness(res);
    }

    @Override
    public double score(IPhenotype phenotype) {
        return 0;
    }

    @Override
    public void evaluatePopulation(Collection<IPhenotype> phenotypes) {
        for (IPhenotype p : phenotypes) {
            evaluate(p);
        }
    }

    private class FireCounter implements FireListener {

        int count = 0;

        @Override
        public void fired() {
            count++;
        }
    }

}
