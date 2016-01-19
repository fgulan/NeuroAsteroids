package hr.fer.zemris.sm.evolution.evaluators.multiobjective;

import hr.fer.zemris.sm.evolution.evaluators.EvaluatorException;
import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Fredi Šarić on 15.01.16.
 */
public class MultiObjectiveEvaluator implements IEvaluator {

    private MultiObjectiveTaskFactory factory;

    private ExecutorService executors;

    private int inputNodeCount;

    private int outputNodeCount;

    private int numberOfObjectives;

    private double reducingFactor;

    private ShareFunction shareFunction;

    private List<Callable<ParetoFrontierIndividual>> tasksList;

    private Set<ParetoFrontierIndividual> resultSet;

    public MultiObjectiveEvaluator(MultiObjectiveTaskFactory factory, ShareFunction shareFunction,
                                   int inputNodeCount, int outputNodeCount,
                                   int popSize, int numberOfObjectives,
                                   double reducingFactor) {
        this.factory = factory;
        this.shareFunction = shareFunction;

        int poolSize = Runtime.getRuntime().availableProcessors();
        this.executors = Executors.newFixedThreadPool(poolSize);

        this.inputNodeCount = inputNodeCount;
        this.outputNodeCount = outputNodeCount;
        this.tasksList = new ArrayList<>(popSize);
        this.resultSet = new HashSet<>(popSize);
        this.numberOfObjectives = numberOfObjectives;

        if(reducingFactor > 1 || reducingFactor < 0 ) {
            throw new EvaluatorException("Reducing factor must be from interval (0, 1)");
        }

        this.reducingFactor = reducingFactor;
    }

    public void shutDownExecutors() {
        this.executors.shutdown();
    }

    @Override
    public int getInputNodeCount() {
        return inputNodeCount;
    }

    @Override
    public int getOutputNodeCount() {
        return outputNodeCount;
    }

    @Override
    public void evaluate(IPhenotype phenotype) {
        throw new NotImplementedException();
    }

    @Override
    public void evaluatePopulation(Collection<IPhenotype> phenotypes) {
        //Fills a result list with a evaluated results
        evaluate(phenotypes);

        //Order individuals in fronts
        Map<Integer, Set<ParetoFrontierIndividual>> fronts = createFronts();

        //Assign fitness to every genotype
        assignFitness(fronts);
    }

    private void evaluate(Collection<IPhenotype> phenotypes) {
        resultSet.clear();
        tasksList.clear();
        phenotypes.forEach(p -> tasksList.add(factory.createTask(p)));

        EvaluatorException exception = null;
        try {
            List<Future<ParetoFrontierIndividual>> results = executors.invokeAll(tasksList);
            for(Future<ParetoFrontierIndividual> f : results) {
                resultSet.add(f.get());
            }
        } catch (Exception e) {
            if(exception  == null) {
                exception = new EvaluatorException();
            }
            exception.addSuppressed(e);
        }
        if(exception != null) {
            throw exception;
        }
    }

    private Map<Integer, Set<ParetoFrontierIndividual>> createFronts() {
        Map<Integer, Set<ParetoFrontierIndividual>> fronts = new HashMap<>();
        fronts.put(0, new HashSet<>()); //Put first front in front map(that is the best front)

        int rank = 0;
        for(ParetoFrontierIndividual p : resultSet) {   //O(n^2)
            for(ParetoFrontierIndividual q : resultSet) {
                if(dominates(p, q)) {               //If p dominates q
                    p.addToDominationSet(q);
                } else if(dominates(q, p)) {        //If q dominates p
                    p.incrementDominatedBy();
                }
            }
            if(p.getDominatedBy() == 0) {           //If p is not dominated by anyone
                fronts.get(rank).add(p);
            }
        }

        do {
            rank++;
            fronts.put(rank, new HashSet<>());

            for(ParetoFrontierIndividual p : fronts.get(rank - 1)) {         //For every individual in previous front
                for(ParetoFrontierIndividual q : p.getDominationSet()) {     //Reduce dominated by of its children
                    q.decrementDominatedBy();
                    if(q.getDominatedBy() == 0) {                            //And if they are not dominated anymore
                        fronts.get(rank).add(q);                             //Put them in current front
                    }
                }
            }
        } while (!fronts.get(rank).isEmpty());

        fronts.remove(rank);    //Remove empty front

        return fronts;
    }

    private boolean dominates(ParetoFrontierIndividual p, ParetoFrontierIndividual q) {
        int counter = 0;
        double[] pObjectiveFitness = p.getObjectiveFitnesses();
        double[] qObjectiveFitness = q.getObjectiveFitnesses();

        for(int i = 0, n = numberOfObjectives; i < n; i++) {
            if(pObjectiveFitness[i] < qObjectiveFitness[i]) return false;
            if(pObjectiveFitness[i] > qObjectiveFitness[i]) counter++;
        }

        return counter != 0;
    }

    private void assignFitness(Map<Integer, Set<ParetoFrontierIndividual>> fronts) {
        double fit = fronts.size();
        double fitMin = fit;        //First front will have at most fitness equal to number of fronts
        for(int i = 0, n=fronts.size(); i < n; i++) {
            Set<ParetoFrontierIndividual> front = fronts.get(i);

            for(ParetoFrontierIndividual p : front) {
                double nc = 0; //Niching factor for p

                for (ParetoFrontierIndividual q : front) {
                    nc += shareFunction.calculate(p,q);
                }

                Genotype g = p.getGenotype();
                g.setFitness(fit / nc);         //Reduce fitness of genotype by its niching factor

                if(g.getFitness() < fitMin) {   //If that is the smallest fitness by far in current front
                    fitMin = g.getFitness();    //update minimal fitness
                }
            }

            fit = fitMin * reducingFactor;      //Set fitness for next front
        }
    }

    @Override
    public double score(IPhenotype phenotype) {
        return 0;
    }
}
