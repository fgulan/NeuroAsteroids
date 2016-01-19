package hr.fer.zemris.sm.evolution.evaluators.multiobjective;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Fredi Šarić on 15.01.16.
 */
public class ParetoFrontierIndividual {

    private Genotype genotype;

    private double[] objectiveFitnesses;

    private Set<ParetoFrontierIndividual> dominationSet;

    private int dominatedBy;

    public ParetoFrontierIndividual(Genotype genotype, double[] objectiveFitnesses) {
        this.genotype = genotype;
        this.objectiveFitnesses = objectiveFitnesses;
        this.dominatedBy = 0;
        this.dominationSet = new HashSet<>();
    }

    public Set<ParetoFrontierIndividual> getDominationSet() {
        return dominationSet;
    }

    public void addToDominationSet(ParetoFrontierIndividual individual) {
        dominationSet.add(individual);
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public double[] getObjectiveFitnesses() {
        return objectiveFitnesses;
    }

    public void incrementDominatedBy() {
        dominatedBy++;
    }

    public void decrementDominatedBy() {
        dominatedBy--;
    }

    public int getDominatedBy() {
        return dominatedBy;
    }

}
