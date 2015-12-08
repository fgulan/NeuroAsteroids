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
 * Created by doctor on 08.12.15..
 */
public class NeuralNetworkController implements IController {

        private IPhenotype phenotype;

        private double fittness = 0;

        private GameWorld world;

        public NeuralNetworkController (IPhenotype phenotype, GameWorld world) {
            this.world = world;
            this.phenotype = phenotype;
        }

        @Override
        public List<Input> getInput() {
            SpriteManager menager = world.getSpriteManager();

            final Ship ship = menager.getShip();
            final IVector shipCenter = ship.getCenter();

            List<AsteroidDistance> closest = menager.getAsteroids().stream().map(a -> {
                IVector asteroidCenter = a.getCenter();

                double dx = shipCenter.get(0) - asteroidCenter.get(0);
                double dy = shipCenter.get(1) - asteroidCenter.get(1);

                return new AsteroidDistance(a, Math.sqrt(dx*dx + dy*dy));
            }).sorted((o1,o2) -> (int)Math.signum(o2.distance - o1.distance)).limit(2).collect(Collectors.toList());

            double aplha1 = getAlpha(ship, closest.get(0));
            double beta1  = getBeta( ship, closest.get(0));
            double d1     = closest.get(0).distance;

            double aplha2 = getAlpha(ship, closest.get(1));
            double beta2  = getBeta( ship, closest.get(1));
            double d2     = closest.get(1).distance;

            double[] output = phenotype.work(new double[] {aplha1, d1, aplha2, d2});

            List<Input> inputs = new ArrayList<>();
            if(output[0] > 0.5) inputs.add(Input.MOVE);
            if(output[1] > 0.5) inputs.add(Input.LEFT);
            if(output[2] > 0.5) inputs.add(Input.RIGHT);
            if(output[3] > 0.5) inputs.add(Input.FIRE);

            return inputs;
        }

        private double getAlpha(Ship ship, AsteroidDistance asteroidDistance) {
            IVector ac = asteroidDistance.a.getCenter();
            IVector sc = ship.getCenter();

            ac.sub(sc);
            double a = Math.atan2(ac.get(1), ac.get(0)); //kut izmedu centra broda i asteroida

            return ship.getCurrentAngle() - a;
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
