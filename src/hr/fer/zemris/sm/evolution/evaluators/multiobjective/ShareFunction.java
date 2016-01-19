package hr.fer.zemris.sm.evolution.evaluators.multiobjective;

/**
 * Created by Fredi Šarić on 15.01.16.
 */
public class ShareFunction {

    private double distanceThreshold;

    private double alpha;

    private IDistanceFunction distanceFunction;

    public ShareFunction(double distanceThreshold, double alpha, IDistanceFunction distanceFunction) {
        this.distanceThreshold = distanceThreshold;
        this.alpha = alpha;
        this.distanceFunction = distanceFunction;
    }

    public double calculate(ParetoFrontierIndividual p, ParetoFrontierIndividual q) {
        double distance = distanceFunction.calculateDistance(p, q);

        if(distance < distanceThreshold) {
            return 1 + Math.pow(distance / distanceThreshold, alpha);
        }
        return 0;
    }

}
