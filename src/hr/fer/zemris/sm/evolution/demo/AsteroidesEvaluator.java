package hr.fer.zemris.sm.evolution.demo;

import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.game.controllers.NeuralNetworkController;
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
        world.play();



        return world.getDestroyedAsteroids(); //* 100 + world.getFrameCount()*0.01;
    }

    @Override
    public double score(IPhenotype phenotype) {
        return 0;
    }


}
