package hr.fer.zemris.sm.evolution.representation.FFANN;

import hr.fer.zemris.sm.evolution.representation.neuralNet.function.IActivationFunction;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;

/**
 * Created by Fredi Šarić on 19.12.15.
 */
public class FFANNGenotypePrototype implements IPhenotype<DoubleArrayGenotype>{


    int[] layers;
    IActivationFunction[] functions;
    double[] output;

    DoubleArrayGenotype genotype;

    public FFANNGenotypePrototype(int[] layers, IActivationFunction[] functions) {
        if(layers.length < 2) {
            throw new IllegalArgumentException("Invalid number of layers in network.");
        }
        if(layers.length != functions.length + 1) {
            throw new IllegalArgumentException("Wrong number of transport functions.");
        }
        this.layers = layers;
        this.functions = functions;
        this.output = new double[layers[layers.length - 1]];    //Output neurons
    }

    public void calcOutputs(double[] inputs, double[] weights ,double[] outputs) {
        double[] in = inputs;
        int wi = 0;
        for(int i = 1; i < layers.length - 1; i++) {
            double[] output = new double[layers[i]];
            int start = wi;
            int end   = wi + (layers[i-1] + 1) * layers[i];
            calculate(in, weights, start, end, functions[i - 1], output);
            wi = end;
            in = output;
        }
        calculate(in, weights, wi, weights.length, functions[functions.length - 1], outputs);
    }

    private void calculate(double[] inputLayer, double[] weights, int start, int end, IActivationFunction function, double[] outputLayer) {
        int weightCounter = start;
        for(int i = 0; i < outputLayer.length; i++) {
            if(weightCounter == end) break;
            double sum = weights[weightCounter++];	//w0 * 1
            for(int j = 0; j < inputLayer.length; j++) {
                sum += inputLayer[j] * weights[weightCounter++];
            }
            outputLayer[i] = function.evaluate(sum);
        }
    }

    @Override
    public DoubleArrayGenotype getGenotype() {
        return genotype;
    }

    public void setGenotype(DoubleArrayGenotype genotype) {
        this.genotype = genotype;
    }

    @Override
    public double[] work(double[] input) {
        calcOutputs(input, genotype.getWeights(), output);
        return output;
    }

}
