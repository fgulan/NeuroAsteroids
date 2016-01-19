package hr.fer.zemris.sm.evolution.representation.neuralNet.function;

import java.io.Serializable;

/**
 * Created by Andrija Milicevic.
 */
public interface IActivationFunction extends Serializable {
    double evaluate(double input);
}
