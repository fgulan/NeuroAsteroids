package hr.fer.zemris.sm.evolution;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class that maps entries with probability and when get method is called
 * every entry has the given probability to be chosen.
 *
 * @param <T>
 */
public class ProbabilityOPeratorBag<T> {

    private static final Random rnd = new Random();

    private Set<ProbabilityEntry<T>> entries;
    private double sum;

    public ProbabilityOPeratorBag() {
        this.entries = new HashSet<>();
        this.sum = 0;
    }

    public void add(ProbabilityEntry<T> operator) {
        entries.add(operator);
        sum = entries.stream().mapToDouble((e) -> e.probability).sum();
    }

    /**
     * Returns some entry from the collection. Entries with greater probability
     * have greater chance to be  chosen.
     * @return
     */
    public T get() {
        double rndVal = rnd.nextDouble();
        double currSum = 0;

        for (ProbabilityEntry<T> e : entries) {
            currSum += e.probability;
            if (currSum / sum > rndVal) {
                return e.entry;
            }
        }
        return null;
    }

    public Set<T> getAllEntries() {
        return entries.stream().map((e) -> e.entry).collect(Collectors.toSet());
    }

    public static class ProbabilityEntry<T> {
        private double probability;
        private T entry;

        public ProbabilityEntry(double probability, T entry) {
            super();
            this.probability = probability;
            this.entry = entry;
        }

        public T getEntry() {
            return entry;
        }
    }
}
