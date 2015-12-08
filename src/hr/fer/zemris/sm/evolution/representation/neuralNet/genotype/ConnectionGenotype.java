package hr.fer.zemris.sm.evolution.representation.neuralNet.genotype;

import java.util.*;

public class ConnectionGenotype extends Genotype {

    private static final Random rand = new Random();

    private int inputNeuronCount;
    private int outputNeuronCount;

    private double specieFitness;

    private List<NeuronConnection> connections = new ArrayList<>();
    private int neuronCount;

    public ConnectionGenotype(int inputNeuronCount, int outputNeuronCount) {
        this.inputNeuronCount = inputNeuronCount;
        this.outputNeuronCount = outputNeuronCount;
    }

    public double getSpecieFitness() {
        return specieFitness;
    }

    public void setSpecieFitness(double specieFitness) {
        this.specieFitness = specieFitness;
    }

    public int getInputNeuronCount() {
        return inputNeuronCount;
    }

    public int getOutputNeuronCount() {
        return outputNeuronCount;
    }

    public int getConnectionCount() {
        return connections.size();
    }

    public int getNeuronCount() {
        return neuronCount;
    }

    public void setNeuronCount(int count) {
        neuronCount = count;
    }

    public int addNeuron() {
        neuronCount++;
        return neuronCount - 1;
    }

    public void addConnection(NeuronConnection connection) {

        int position = Collections.binarySearch(connections, connection);

        if (position >= 0) {
            return;
        }

        connections.add(-position - 1, connection);
    }

    public void removeConnection(NeuronConnection connection) {
        int position = Collections.binarySearch(connections, connection);

        if (position < 0) {
            return;
        }

        connections.remove(position);
    }

    public NeuronConnection getConnection(int inNeuronIndex, int outNeuronIndex) {
        int position = Collections.binarySearch(connections, new NeuronConnection(inNeuronIndex, outNeuronIndex,0.0, false));

        if (position >= 0) {
            return connections.get(position);
        }

        return null;
    }

    public NeuronConnection getRandomConnection() {
        return connections.get((int) (rand.nextDouble() * connections.size()));
    }

    public NeuronConnection getRandomActiveOutputConnection() {
        int outputConnectionCounter = 0;

        for (NeuronConnection connection : connections) {
            if (connection.isActive() && isOutputNeuron(connection.getInNeuron())) {
                outputConnectionCounter++;
            }
        }

        if (outputConnectionCounter == 0) {
            return null;
        }

        outputConnectionCounter = (int) (rand.nextDouble() * outputConnectionCounter);

        for (NeuronConnection connection : connections) {
            if (connection.isActive() && isOutputNeuron(connection.getInNeuron())) {

                if (outputConnectionCounter == 0) {
                    return connection;
                }

                outputConnectionCounter--;
            }
        }

        return null;
    }

    public boolean isInputNeuron(int inNeuron) {
        return inNeuron < inputNeuronCount;
    }
    public boolean isOutputNeuron(int inNeuron) {
        return inNeuron >= inputNeuronCount && inNeuron < outputNeuronCount + inputNeuronCount;
    }

    @Override
    public ConnectionGenotype copy() {
        ConnectionGenotype genotype = new ConnectionGenotype(inputNeuronCount, outputNeuronCount);

        for (NeuronConnection connection : connections) {
            genotype.addConnection(connection.copy());
        }

        genotype.neuronCount = this.neuronCount;
        genotype.setFitness(getFitness());
        //genotype.setSpecieFitness(getSpecieFitness());

        return genotype;
    }

    @Override
    public Iterator<NeuronConnection> iterator() {
        return connections.iterator();
    }

}
