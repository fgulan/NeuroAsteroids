package hr.fer.zemris.sm.evolution.evaluators.multiobjective;

/**
 * Created by Fredi Šarić on 15.01.16.
 */
public interface IDistanceFunction {

    double calculateDistance(ParetoFrontierIndividual p, ParetoFrontierIndividual q);



}
