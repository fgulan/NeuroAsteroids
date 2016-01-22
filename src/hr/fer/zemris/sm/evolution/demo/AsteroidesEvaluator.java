package hr.fer.zemris.sm.evolution.demo;

import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.game.controllers.NeuralNetworkController;
import hr.fer.zemris.sm.game.world.GameEvent;
import hr.fer.zemris.sm.game.world.LimitedFramesSimulationWorld;
import static hr.fer.zemris.sm.game.Constants.*;

import java.util.Collection;

/**
 * Created by Andrija Milicevic.
 */
public class AsteroidesEvaluator implements IEvaluator {

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
            NeuralNetworkController controller = new NeuralNetworkController(phenotype, world);
            world.setController(controller);
            world.initialize();

            world.addListener(GameEvent.MISSILE_FIRED, e -> fireCounter++);
            world.play();
            res += controller.fittness * 50;
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
}
