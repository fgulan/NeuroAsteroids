package hr.fer.zemris.sm.evolution.representation.FFANN.crossover;

import hr.fer.zemris.sm.evolution.representation.FFANN.DoubleArrayGenotype;
import hr.fer.zemris.sm.evolution.representation.ICrossover;

import static java.lang.Math.*;

import java.util.Random;

/**
 * Implementation of BLX-alpha.
 * @see <a href="http://www.tomaszgwiazda.com/blendX.htm">http://www.tomaszgwiazda.com/blendX.htm</a>
 *
 * Created by Fredi Šarić on 19.12.15.
 */
public class BLXCrossover implements ICrossover<DoubleArrayGenotype> {

    private double alpha;

    private Random rnd;

    public BLXCrossover(double alpha) {
        this.rnd = new Random();
        this.alpha = alpha;
    }


    @Override
    public DoubleArrayGenotype crossover(DoubleArrayGenotype parent1, DoubleArrayGenotype parent2) {
        double[] p1 = parent1.getWeights();
        double[] p2 = parent2.getWeights();

        double[] child = new double[p1.length];
        for(int i = 0; i < child.length; i++) {
            double d   = abs(p1[1] - p2[i]);
            double start = min(p1[i], p2[i]) - alpha * d;
            double end   = max(p1[1], p2[i]) + alpha * d;

            child[i] = start + rnd.nextDouble() * (end - start);
        }

        return new DoubleArrayGenotype(child);
    }
}
