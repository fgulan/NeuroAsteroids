package hr.fer.zemris.sm.evolution.population;

import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.ConnectionGenotype;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Specie implements Iterable<ConnectionGenotype>{;

	private List<ConnectionGenotype> genotypes;
	
	private ConnectionGenotype representative;

	private double speciesFitness;

	public double getSpeciesFitness() {
		return speciesFitness;
	}

	public void setSpeciesFitness(double speciesFitness) {
		this.speciesFitness = speciesFitness;
	}

	public Specie(ConnectionGenotype genotype) {
		this.representative = genotype.copy();
		this.genotypes = new ArrayList<>();
		genotypes.add(genotype);
	}

	@Override
	public Iterator<ConnectionGenotype> iterator() {
		return genotypes.iterator();
	}

	public List<ConnectionGenotype> getGenotypes() {
		return genotypes;
	}

	public ConnectionGenotype getBestIndividual() {
		return genotypes.stream().sorted().findFirst().get();
	}

	public ConnectionGenotype getSpeciesRepresentative() {
		return representative;
	}

	public int size() {
		return genotypes.size();
	}
}
