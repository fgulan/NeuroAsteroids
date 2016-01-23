package hr.fer.zemris.sm.evolution.evaluators.multiobjective;

/**
 * Distance function that measures distance based on objective functions
 * values.
 *
 * Created by Fredi Šarić on 15.01.16.
 */
public class ObjectiveSpaceDistanceFunction implements IDistanceFunction {

    private double power;
    private double[][] bounds;

    public ObjectiveSpaceDistanceFunction(double[][] bounds) {
        this(1/2.0, bounds);
    }

    public ObjectiveSpaceDistanceFunction(double power, double[][] bounds) {
        this.power = power;
        this.bounds = bounds;
    }

    @Override
    public double calculateDistance(ParetoFrontierIndividual p, ParetoFrontierIndividual q) {
        if(p == q) return 0;

        double[] pObjectiveFitnesses = p.getObjectiveFitnesses();
        double[] qObjectiveFitnesses = q.getObjectiveFitnesses();

        //If new individual comes with unexpected values of fitness
        updateBounds(pObjectiveFitnesses);
        updateBounds(qObjectiveFitnesses);

        double distance = 0;
        for(int i = 0; i < pObjectiveFitnesses.length; i++) {
            double ps = pObjectiveFitnesses[i];
            double qs = qObjectiveFitnesses[i];

            distance += Math.pow((ps-qs)/(bounds[i][1] - bounds[i][0]) ,2);
        }

        return Math.pow(distance, power);
    }

    private void updateBounds(double[] objectiveFitnesses) {
        for(int i = 0; i < objectiveFitnesses.length; i++) {
            double[] objectiveBounds = bounds[i];
            if(objectiveFitnesses[i] < objectiveBounds[0]) {
                objectiveBounds[0] = objectiveFitnesses[i];
            } else if(objectiveFitnesses[i] > objectiveBounds[1]) {
                objectiveBounds[1] = objectiveFitnesses[i];
            }
        }
    }
}
