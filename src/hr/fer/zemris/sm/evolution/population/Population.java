package hr.fer.zemris.sm.evolution.population;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Population implements Iterable<Specie> {

    private List<Specie> species = new LinkedList<>();

    @Override
    public Iterator<Specie> iterator() {
        return species.iterator();
    }

    public int getSpeciesCount() {
        return species.size();
    }

    public void addSpecie(Specie specie) {
        species.add(specie);
    }

    public int size() {
        return species.stream().mapToInt((s) -> s.size()).sum();
    }

    public Genotype getBest() {
        return species
                .stream()
                .map(Specie::getBestIndividual)
                .sorted()
                .findFirst()
                .get();
    }
}
