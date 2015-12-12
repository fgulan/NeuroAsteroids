package hr.fer.zemris.sm.evolution.algorithms;

import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.population.Population;
import hr.fer.zemris.sm.evolution.population.Specie;
import hr.fer.zemris.sm.evolution.representation.neuralNet.*;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.ConnectionGenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.NeuronConnection;
import hr.fer.zemris.sm.evolution.representation.neuralNet.mutation.*;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;
import hr.fer.zemris.sm.evolution.selection.ISelection;
import hr.fer.zemris.sm.evolution.selection.TournamentSelection;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Andrija Milicevic.
 */
public class SpeciesAlgorithm implements IAlgorithm {

    private static final int POPULATION_SIZE = 25;
    private static final int MAX_NODE = 20;
    private static final ISpecieCompatibilityOperator speciesCompatibilityOperator = new OneSpecieCompatibility(); //new DeltaDistance(0.6, 3.0, 3.0, 0.02, false);
    private static final Random rand = new Random();
    private final IEvaluator evaluator;
    private final List<Mutation> mutations;
    private final ISelection selection = new TournamentSelection(3);
    private final ICrossover<ConnectionGenotype> crossover = new SlowCrossover();
    private Population population;
    IDecoder decoder = new ConnectionDecoder();

    public SpeciesAlgorithm(IEvaluator evaluator) {
        this.evaluator = evaluator;
        initializePopulation();
        mutations = new LinkedList<>();
        mutations.add(new AddNodeMutation(0.2, MAX_NODE));
        mutations.add(new AddOrEnableConnectionMutation(0.4));
        mutations.add(new AllWeightGaussianMutation(0.8, 0.1));
        //mutations.add(new DisableConnectionMutation(0.05));
        //mutations.add(new DeleteConnectionMutation(0.05));
    }

    private void initializePopulation() {
        int input = evaluator.getInputNodeCount();
        int output = evaluator.getOutputNodeCount();
        ConnectionGenotype sampleGenotype = new ConnectionGenotype(input, output);
        for (int i = 0; i < input; i++) {
            for (int j = input; j < input + output; j++) {
                sampleGenotype.addConnection(new NeuronConnection(j, i, 0.0, true));
            }
        }

        sampleGenotype.setNeuronCount(input + output);

        ConnectionGenotype copyGenotype;
        List<ConnectionGenotype> genotypes = new LinkedList<>();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            copyGenotype = sampleGenotype.copy();

            for (NeuronConnection connection : copyGenotype) {
                connection.setWeight(rand.nextDouble() * 4.0 - 2.0);
            }

            genotypes.add(copyGenotype);
        }
        evaluator.evaluatePopulation(genotypes.stream().map(g -> {
            return decoder.decode(g);
        }).collect(Collectors.toList()));

        population = putAllInSpecies(genotypes);
    }

    private Population putAllInSpecies(List<ConnectionGenotype> genotypes) {
        Population newPopulation = new Population();

        boolean isAdded;

        for (ConnectionGenotype g : genotypes) {
            isAdded = false;
            for (Specie ns : newPopulation) {
                if (speciesCompatibilityOperator.isCompatibile(g, ns)) {
                    ns.getGenotypes().add(g);
                    isAdded = true;
                    break;
                }
            }

            if (!isAdded) {
                newPopulation.addSpecie(new Specie(g));
            }
        }
        return newPopulation;
    }

    @Override
    public void nextGeneration() {
        double sumFitness;
        double totalFitness = 0;

        for (Specie s : population) {
            sumFitness = 0;
            for (ConnectionGenotype g : s) {
                g.setSpecieFitness(g.getFitness() / s.size());
                sumFitness += g.getSpecieFitness();
            }

            s.setSpeciesFitness(sumFitness);
            totalFitness += sumFitness;
        }

        int[] offspringCount = new int[population.size()];

        int cnt = 0;
        int totalOffspring = 0;

        for (Specie s : population) {
            offspringCount[cnt] = (int) Math.ceil(s.getSpeciesFitness() / totalFitness * POPULATION_SIZE);
            totalOffspring += offspringCount[cnt];
            cnt++;
        }
        int pos;

        /*
        while (totalOffspring > maxPop) {

            pos = ((int) (rand.nextDouble() * cnt));
            while (offspringCount[pos] <= 0) {
                pos = ((int) (rand.nextDouble() * cnt));
            }
            offspringCount[pos]--;
            totalOffspring--;
        }
        */

        List<ConnectionGenotype> newGenotypes = new ArrayList<>();
        ConnectionGenotype g2;
        cnt = 0;
        for (Specie s : population) {
            for (int i = 0; i < offspringCount[cnt]; i++) {

                ConnectionGenotype p1 = (ConnectionGenotype)selection.select(s.getGenotypes());
                ConnectionGenotype p2 = (ConnectionGenotype)selection.select(s.getGenotypes());

                ConnectionGenotype child = crossover.crossover(p1, p2);
                for (Mutation m : mutations) {
                    m.mutate(child);
                }

                newGenotypes.add(child);
            }

            cnt++;
        }

        evaluator.evaluatePopulation(newGenotypes.stream().map(g -> decoder.decode(g)).collect(Collectors.toList()));

        newGenotypes.add((ConnectionGenotype)population.getBest());

        population = putAllInSpecies(newGenotypes);

        //System.out.println(" SP : " + population.getSpeciesCount());
    }

    @Override
    public Genotype getBestGenotype() {
        return population.getBest();
    }

    @Override
    public IPhenotype getBestPhenotype() {
        return decoder.decode(population.getBest());
    }
}
