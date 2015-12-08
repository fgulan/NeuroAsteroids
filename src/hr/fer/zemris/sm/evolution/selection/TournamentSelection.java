package hr.fer.zemris.sm.evolution.selection;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

import java.util.List;
import java.util.Random;

public class TournamentSelection implements ISelection<Genotype> {

    private int tournamentSize;

    private Random rnd = new Random();

    public TournamentSelection(int tournamentSize) {
        super();
        this.tournamentSize = tournamentSize;
        this.rnd = new Random();
    }

    @Override
    public Genotype select(List<Genotype> genotypes) {
        int tSize = Math.min(tournamentSize, genotypes.size());

        while (genotypes.size() < tSize) {
            genotypes.add(genotypes.get((int) (rnd.nextDouble() * genotypes.size())));
        }
        //Love java 8 streams :D
        return genotypes.stream().sorted().findFirst().get();
    }
}