package hr.fer.zemris.sm.evolution.representation.FFANN;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fredi Šarić on 19.12.15.
 */
public class FFANNPopulation {

    private List<DoubleArrayGenotype> individuals;

    public FFANNPopulation(int popSize) {
        this.individuals = new ArrayList<>(popSize);
    }



}
