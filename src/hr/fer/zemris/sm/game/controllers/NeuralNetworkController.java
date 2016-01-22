package hr.fer.zemris.sm.game.controllers;

import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.game.managers.SpriteManager;
import hr.fer.zemris.sm.game.models.Asteroid;
import hr.fer.zemris.sm.game.models.Ship;
import hr.fer.zemris.sm.game.physics.IVector;
import hr.fer.zemris.sm.game.world.GameWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Created by doctor on 08.12.15..
 */
public class NeuralNetworkController extends AbstractPhenotypeController {

    public double fittness = 0;

    public NeuralNetworkController(IPhenotype phenotype, GameWorld world) {
        super(phenotype);
        this.world = world;
    }

    @Override
    public List<Input> getInput() {
        SpriteManager manager = world.getSpriteManager();

        final Ship ship = manager.getShip();
        final IVector shipCenter = ship.getCenter();

        List<AsteroidDistance> closest = manager.getAsteroids().stream().map(a -> {
            IVector asteroidCenter = a.getCenter();

            double dx = shipCenter.get(0) - asteroidCenter.get(0);
            double dy = shipCenter.get(1) - asteroidCenter.get(1);

            return new AsteroidDistance(a, Math.sqrt(dx * dx + dy * dy));
        }).sorted((o1, o2) -> (int) Math.signum(o1.distance - o2.distance)).limit(2).collect(Collectors.toList());

        double aplha1 = calcDistance(ship, closest.get(0).a);
        double beta1 = getBeta(ship, closest.get(0));
        double d1 = closest.get(0).distance;

        //double aplha2 = getAlpha(ship, closest.get(1));
        //double beta2  = getBeta( ship, closest.get(1));
        //double d2     = closest.get(1).distance;

        double[] output = phenotype.work(new double[]{aplha1, d1});

        List<Input> inputs = new ArrayList<>();
        if (output[0] > 0.5) {
            //inputs.add(Input.MOVE);
            //if(d1 < 60 ) fittness += 5;
            //else fittness-=2;
            //if(d2 < 60 ) fittness += 2;
            //else fittness-=1;
        }

        int move = 0;
        if (output[1] > 0.5) {
            inputs.add(Input.LEFT);
            if (aplha1 > 0.0) fittness += 0.5;
            else fittness -= 0.4;
            move++;
        }
        if (output[2] > 0.5) {
            inputs.add(Input.RIGHT);
            if (aplha1 < 0.0) fittness += 0.5;
            else fittness -= 0.4;
            move++;
        }

        if (move == 0 || move == 2) {
            fittness--;
        }

        if (output[3] > 0.5) {
            inputs.add(Input.FIRE);

        }

        return inputs;
    }

    private double getBeta(Ship ship, AsteroidDistance asteroidDistance) {
        return 0;
    }


    private class AsteroidDistance {
        Asteroid a;
        double distance;

        public AsteroidDistance(Asteroid a, double distance) {
            this.a = a;
            this.distance = distance;
        }
    }
}
