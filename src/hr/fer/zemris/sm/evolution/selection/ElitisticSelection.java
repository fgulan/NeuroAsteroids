package hr.fer.zemris.sm.evolution.selection;

import hr.fer.zemris.sm.evolution.representation.FFANN.DoubleArrayGenotype;
import hr.fer.zemris.sm.evolution.representation.neuralNet.genotype.Genotype;

import java.util.List;

/**
 * Created by Fredi Šarić on 22.12.15.
 */
public class ElitisticSelection implements ISelection<Genotype> {

    @Override
    public Genotype select(List<Genotype> genotypes) {
        return genotypes.stream().max((s1,s2) -> (int)Math.signum(s1.getFitness() - s2.getFitness())).get();
    }
}
