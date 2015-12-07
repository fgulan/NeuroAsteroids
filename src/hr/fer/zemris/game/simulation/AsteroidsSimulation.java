package hr.fer.zemris.game.simulation;

import hr.fer.zemris.game.world.SimulationWorld;

public class AsteroidsSimulation {

    private static SimulationWorld gameWorld;

    public static void main(String[] args) {
        gameWorld = new SimulationWorld(8000, 6000, 15);
        gameWorld.initialize();
        long startTime = System.currentTimeMillis();
        gameWorld.play();

        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(estimatedTime / 1000.0);
    }

}
