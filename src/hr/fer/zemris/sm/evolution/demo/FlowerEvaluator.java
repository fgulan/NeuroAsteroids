package hr.fer.zemris.sm.evolution.demo;

import hr.fer.zemris.sm.evolution.evaluators.EvaluatorException;
import hr.fer.zemris.sm.evolution.evaluators.IEvaluator;
import hr.fer.zemris.sm.evolution.representation.neuralNet.phenotype.IPhenotype;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Andrija Milicevic.
 */
public class FlowerEvaluator implements IEvaluator {

    List<double[]> input;
    List<double[]> output;

    public FlowerEvaluator(String filename) {

        input = new ArrayList<>();
        output = new ArrayList<>();

        Path path = Paths.get(filename);
        Scanner scanner = null;
        try {
            scanner = new Scanner(path);
        } catch (IOException e) {
            throw new IllegalArgumentException("FAIL FLOWER INPUT!");
        }

        String[] line;
        String[] tmp;

        double[] pero;

        while (scanner.hasNextLine()) {
            line = scanner.nextLine().split(":");
            line[0] = line[0].substring(1, line[0].length() - 1);
            line[1] = line[1].substring(1, line[1].length() - 1);

            tmp = line[0].split(",");
            pero = new double[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                pero[i] = Double.parseDouble(tmp[i]);
            }

            input.add(pero);

            tmp = line[1].split(",");
            pero = new double[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                pero[i] = Double.parseDouble(tmp[i]);
            }

            output.add(pero);
        }
    }

    @Override
    public double evaluate(IPhenotype phenotype) throws EvaluatorException {
        double returnValue = 0;
        double[] outputValue;

        for (int i = 0; i < input.size(); i++) {
            outputValue = phenotype.work(input.get(i));

            for (int j = 0; j < 3; j++) {
                returnValue += Math.pow(outputValue[j] - output.get(i)[j], 2.0);
            }
        }

        returnValue /= input.size();
        returnValue += 0.001;

        returnValue = 1.0 / returnValue;

        if (score(phenotype) == 150) {
            returnValue += 10000;
        } //else {
            //returnValue *= (1.0 + 1 / (150 - score(phenotype) + 0.01));
        //}

        return returnValue;
    }

    @Override
    public double score(IPhenotype phenotype) {

        int returnValue = 0;
        double[] outputValue;

        int correct;

        for (int i = 0; i < input.size(); i++) {
            outputValue = phenotype.work(input.get(i));

            correct = 0;

            for (int j = 0; j < 3; j++) {
                if (Math.round(outputValue[j]) == Math.round(output.get(i)[j])) {
                    correct++;
                }
            }

            if (correct == 3) {
                returnValue++;
            }
        }

        return returnValue;
    }

    @Override
    public int getInputNodeCount() {
        return 4;
    }

    @Override
    public int getOutputNodeCount() {
        return 3;
    }
}
