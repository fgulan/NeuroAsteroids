package hr.fer.zemris.sm.evolution.demo;

import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.game.controllers.NeuralNetworkController;
import hr.fer.zemris.sm.game.world.Listeners.FireListener;
import hr.fer.zemris.sm.game.world.SimulationWorld;

/**
 * Created by Andrija Milicevic.
 */
public class AsteroidesEvaluator implements IEvaluator {

    SimulationWorld world;

    @Override
    public int getInputNodeCount() {
        return 4;
    }

    @Override
    public int getOutputNodeCount() {
        return 4;
    }

    @Override
    public double evaluate(IPhenotype phenotype) {
        world = new SimulationWorld(800, 600, 20, null);
        NeuralNetworkController controller = new NeuralNetworkController(phenotype, world);
        world.setController(controller);
        world.initialize();

        FireCounter fc = new FireCounter();
        world.registerFireListener(fc);
        world.play();

        int acc = 0;
        if(fc.count != 0)  {
            acc = world.getDestroyedAsteroids() / fc.count;
        }

        return acc;
    }

    @Override
    public double score(IPhenotype phenotype) {
        return 0;
    }


    private class FireCounter implements FireListener {

        int count = 0;

        @Override
        public void fired() {
            count++;
        }
    }

}
