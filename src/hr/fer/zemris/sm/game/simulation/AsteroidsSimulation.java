package hr.fer.zemris.sm.game.simulation;

import hr.fer.zemris.sm.game.world.SimulationWorld;

public class AsteroidsSimulation {

    private static SimulationWorld gameWorld;

    public static void main(String[] args) {
        gameWorld.initialize();
        long startTime = System.currentTimeMillis();
        gameWorld.play();

        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(estimatedTime / 1000.0);
    }

}
