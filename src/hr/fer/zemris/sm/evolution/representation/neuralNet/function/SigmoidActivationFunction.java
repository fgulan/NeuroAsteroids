package hr.fer.zemris.sm.evolution.representation.neuralNet.function;

/**
 * Created by Andrija Milicevic.
 */
public class SigmoidActivationFunction implements IActivationFunction {
    @Override
    public double evaluate(double input) {
        return 1.0 / ( 1 + Math.exp(-input));
    }
}
