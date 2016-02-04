package hr.fer.zemris.sm.evolution.evaluators.multiobjective;

/**
 * Represents distance function used for calculating distance between individuals.
 *
 * Created by Fredi Šarić on 15.01.16.
 */
public interface IDistanceFunction {

    double calculateDistance(ParetoFrontierIndividual p, ParetoFrontierIndividual q);



}
