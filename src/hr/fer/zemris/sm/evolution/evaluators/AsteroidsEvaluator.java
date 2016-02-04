package hr.fer.zemris.sm.evolution.evaluators;

import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.game.controllers.FFANNController11;
import hr.fer.zemris.sm.game.world.GameEvent;
import hr.fer.zemris.sm.game.world.LimitedFramesSimulationWorld;
import static hr.fer.zemris.sm.game.Constants.*;

import java.util.Collection;

/**
 * Evaluator for the neural network controllers.
 *
 * Created by Andrija Milicevic.
 */
public class AsteroidsEvaluator implements IEvaluator {

    LimitedFramesSimulationWorld world;

    private int fireCounter = 0;

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
            world = new LimitedFramesSimulationWorld(800, 600, AI_SIMULATION_PLAY_ASTEROIDS_NUMBER, STARS_NUMBER, 10_000);
            FFANNController11 controller = new FFANNController11(phenotype);
            controller.setWorld(world);
            world.setController(controller);
            world.initialize();

            world.addListener(GameEvent.MISSILE_FIRED, e -> fireCounter++);
            world.play();

            //TODO: change this
            res += 50;
        }

        phenotype.getGenotype().setFitness(res);
    }

    @Override
    public void evaluatePopulation(Collection<IPhenotype> phenotypes) {
        phenotypes.forEach(this::evaluate);
    }
}
