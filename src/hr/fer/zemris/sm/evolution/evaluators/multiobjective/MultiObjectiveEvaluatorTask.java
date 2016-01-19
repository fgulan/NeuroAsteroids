package hr.fer.zemris.sm.evolution.evaluators.multiobjective;

import hr.fer.zemris.sm.evolution.evaluators.multiThreading.EvaluatorTask;
import hr.fer.zemris.sm.game.controllers.FFANNController10;
import hr.fer.zemris.sm.game.controllers.FFANNController11;
import hr.fer.zemris.sm.game.world.LimitedFramesSimulationWorld;

/**
 *
 * Created by Fredi Šarić on 15.01.16.
 */
public class MultiObjectiveEvaluatorTask extends EvaluatorTask<ParetoFrontierIndividual> {

    private final static int NUM_OF_SIM_RUNS = 5;
    private final static int WORLD_WIDTH = 1600;
    private final static int WORLD_HEIGHT = 1600;
    private final static int NUMBER_OF_COMETS = 5;
    private final static int NUMBER_OF_STARS = 1;
    private final static int MAX_NUMBER_OF_FRAMES = 10_000;

    /**
     * This is purely here for the purpose of clean code.
     * This number is defined by grading of phenotype which is done in
     * call() method!
     */
    public static int NUM_OF_OBJECTIVES = 2;

    @Override
    public ParetoFrontierIndividual call() throws Exception {

        //Just in case
        if(NUM_OF_SIM_RUNS < 2) throw new RuntimeException("Number of sim runs must be at least 2");

        FFANNController10 controller = new FFANNController10(getPhenotype());

        double[][] objectiveFitnesses = new double[NUM_OF_SIM_RUNS][NUM_OF_OBJECTIVES];
        for(int i = 0 ; i < NUM_OF_SIM_RUNS; i++) {
            LimitedFramesSimulationWorld world = new LimitedFramesSimulationWorld(WORLD_WIDTH, WORLD_HEIGHT,
                    NUMBER_OF_COMETS, NUMBER_OF_STARS, MAX_NUMBER_OF_FRAMES);
            controller.setWorld(world);

            world.setController(controller);
            world.initialize();
            world.play();


            if(world.getFrames() <= 180) {  //Those that do not survive for 3 s will respawn
                i--;
                continue;
            }

            int destroyed = world.getAsteroidsDestroyed();

            objectiveFitnesses[i][0] = world.getStarsCollected();
            if(world.getShootsFired() == 0) {
                objectiveFitnesses[i][1] = 0;
            } else {
                objectiveFitnesses[i][1] = (destroyed * destroyed) / (double) world.getShootsFired();
            }

            world.setController(null); //make it easy on GC (still bad implementation)
        }

        controller.disconnect();

        int minIndex = findMin(objectiveFitnesses, 0.0);
        int secondMinIndex = findMin(objectiveFitnesses, minIndex);

        double[] objFit = new double[NUM_OF_OBJECTIVES];
        for(int i = 0; i < NUM_OF_OBJECTIVES; i++) {
            objFit[i] = (objectiveFitnesses[minIndex][i] + objectiveFitnesses[secondMinIndex][i]) / 2;
        }

        return new ParetoFrontierIndividual(getPhenotype().getGenotype(), objFit);
    }

    private int findMin(double[][] objectiveFitnesses, double greaterThan) {
        double min = Double.MIN_VALUE;
        int minIndex = 0;
        for(int i = 0; i < objectiveFitnesses.length; i++) {
            double sum = objectiveFitnesses[i][0] + objectiveFitnesses[i][1];
            if(sum < min && sum > greaterThan) {
                minIndex = i;
                min = sum;
            }
        }

        return minIndex;
    }
}
