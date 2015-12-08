package hr.fer.zemris.sm.evolution.selection;


import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

import java.util.List;

public interface ISelection<T extends Genotype>{

    T select(List<T> genotypes);
}
