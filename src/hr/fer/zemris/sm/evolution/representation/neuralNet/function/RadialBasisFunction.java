package hr.fer.zemris.sm.evolution.representation.neuralNet.function;

/**
 * Created by Fredi Šarić on 22.12.15.
 */
public class RadialBasisFunction implements IActivationFunction {

    private double center;

    public RadialBasisFunction(double center) {
        this.center = center;
    }

    @Override
    public double evaluate(double input) {
        return Math.exp(-Math.pow(input - center, 2));
    }
}
