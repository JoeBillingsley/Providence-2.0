package neural_network;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * An implementation of a multi-layer feed-forward perceptron neural network.
 * <p>
 * A variable number of inputs and outputs can be provided. The network supports one or more hidden layers
 * with the same number of nodes in each hidden layer.
 * <p>
 * A logistic function is used for the step function when calculating output values.
 * <p>
 * Initial weights are generated using the provided weight initialiser or {@link RandomWeightInit} if one is not
 * provided.
 * <p>
 * Created by Joseph Billingsley on 26/10/2015.
 */
public class FeedForwardPerceptron implements INeuralNetwork {

    private final int
            numberOfInputNodes,
            numberOfNodesInHiddenLayer,
            numberOfOutputNodes,
            numberOfHiddenLayers;

    private final Double[] weights;
    private final Double[] thresholds;
    private final IStepFunction internalStepFunc, outputStepFunc;
    private Double[][] values;

    public FeedForwardPerceptron(int numberOfInputs, int numberOfNodesInHiddenLayer, int numberOfOutputs,
                                 int numberOfHiddenLayers, Double defaultThreshold) {
        this(numberOfInputs,
                numberOfNodesInHiddenLayer,
                numberOfOutputs,
                numberOfHiddenLayers,
                defaultThreshold,
                new RandomWeightInit(),
                new LogisticStepFunc(),
                new LinearStepFunc());
    }

    public FeedForwardPerceptron(int numberOfInputs, int numberOfNodesInHiddenLayer, int numberOfOutputs,
                                 int numberOfHiddenLayers, Double defaultThreshold, IWeightInitialiser weightInitialiser) {
        this(numberOfInputs,
                numberOfNodesInHiddenLayer,
                numberOfOutputs,
                numberOfHiddenLayers,
                defaultThreshold,
                weightInitialiser,
                new LogisticStepFunc(),
                new LinearStepFunc());
    }

    /**
     * Constructs the neural network with random weights and the required number of inputs and outputs.
     *
     * @param numberOfInputs             The number of inputs the network will take
     * @param numberOfNodesInHiddenLayer The number of nodes in each hidden layer
     * @param numberOfOutputs            The number of outputs the network will produce
     * @param numberOfHiddenLayers       The number of hidden layers between the input and output layers
     * @param weightInitialiser          The function to use for setting initial weights
     * @param internalStepFunc           The step function to use for internal weights
     * @param outputStepFunc             The step function to use for weights to output nodes
     */
    public FeedForwardPerceptron(int numberOfInputs, int numberOfNodesInHiddenLayer, int numberOfOutputs,
                                 int numberOfHiddenLayers, Double defaultThreshold,
                                 IWeightInitialiser weightInitialiser,
                                 IStepFunction internalStepFunc, IStepFunction outputStepFunc) {

        // region Argument checks
        if (numberOfInputs < 1)
            throw new IllegalArgumentException("There must be at least one input into the neural network");

        if (numberOfNodesInHiddenLayer < 1)
            throw new IllegalArgumentException("There must be at least one node in each hidden layer in the neural network");

        if (numberOfOutputs < 1)
            throw new IllegalArgumentException("There must be at least one output from the neural network");

        if (numberOfHiddenLayers < 1)
            throw new IllegalArgumentException("There must be at least one hidden layer in the neural network");
        // endregion

        this.numberOfInputNodes = numberOfInputs;
        this.numberOfNodesInHiddenLayer = numberOfNodesInHiddenLayer;
        this.numberOfOutputNodes = numberOfOutputs;
        this.numberOfHiddenLayers = numberOfHiddenLayers;

        this.internalStepFunc = internalStepFunc;
        this.outputStepFunc = outputStepFunc;

        int numberOfWeights = 0;

        int numberOfLayers = getNumberOfLayers();

        values = new Double[numberOfLayers][];

        int totalNodes = 0;

        for (int i = 1; i < numberOfLayers; i++) {
            int nodesInLayer = countNodesInLayer(i);
            values[i] = new Double[nodesInLayer];
            totalNodes += nodesInLayer;
        }

        thresholds = new Double[totalNodes];
        Arrays.fill(thresholds, defaultThreshold);

        for (int i = 0; i < numberOfLayers - 1; i++)
            numberOfWeights += countWeightsInLayer(i);

        weights = weightInitialiser.init(numberOfWeights, -5, 5);
    }

    @Override
    public INeuralNetwork copy() {

        FeedForwardPerceptron copy =
                new FeedForwardPerceptron(
                        numberOfInputNodes,
                        numberOfNodesInHiddenLayer,
                        numberOfOutputNodes,
                        numberOfHiddenLayers,
                        1.0);

        for (int i = 0; i < getWeights().length; i++) {
            copy.setWeight(i, getWeight(i));
        }

        return copy;
    }

    public Double[] executeNetwork(Double[] inputs) {

        // region Argument checks
        if (inputs.length != numberOfInputNodes)
            throw new IllegalArgumentException("The number of inputs provided does not match the number of inputs the network was initialised with.");
        // endregion

        values[0] = inputs;

        int outputLayerIndex = getOutputLayerIndex();

        int sumNodes = 0;

        for (int i = 1; i <= outputLayerIndex; i++) {

            int nodesInLayer = countNodesInLayer(i);

            IStepFunction stepFunction =
                    i != outputLayerIndex ? internalStepFunc : outputStepFunc;

            for (int j = 0; j < nodesInLayer; j++) {
                Double[] weights = getWeightsToNode(i, j);
                values[i][j] = calculateNodeValue(values[i - 1], weights, thresholds[sumNodes + j], stepFunction);
            }

            sumNodes += nodesInLayer;
        }

        return values[outputLayerIndex].clone();
    }

    public int getOutputLayerIndex() {
        return numberOfHiddenLayers + 1;
    }

    @Override
    public Double getWeight(int index) {
        return weights[index];
    }

    public void setWeight(int index, Double value) {
        weights[index] = value;
    }

    @Override
    public Double[] getWeights() {
        return weights;
    }

    public Double[] getThresholds() {
        return thresholds;
    }

    public void setThreshold(int index, Double value) {
        thresholds[index] = value;
    }

    public Double getThreshold(int index) {
        return thresholds[index];
    }

    @NotNull
    private Double calculateNodeValue(Double[] inputs, Double[] weights, Double threshold, IStepFunction stepFunction) {

        // region Argument checks
        if (inputs.length != weights.length)
            throw new IllegalArgumentException("The number of inputs does not match the number of weights");
        // endregion

        double total = 0;

        for (int i = 0; i < inputs.length; i++) {
            total += inputs[i] * weights[i];
        }

        total -= threshold;

        return stepFunction.response(total);
    }

    public int countNodesInLayer(int index) {

        int outputLayerIndex = getOutputLayerIndex();

        // region Argument checks
        if (index > outputLayerIndex || index < 0)
            throw new IndexOutOfBoundsException();
        // endregion

        if (index == 0)
            return numberOfInputNodes;

        if (index == outputLayerIndex)
            return numberOfOutputNodes;

        return numberOfNodesInHiddenLayer;
    }

    public int countWeightsInLayer(int index) {

        // region Argument checks
        if (index >= getOutputLayerIndex())
            throw new IndexOutOfBoundsException();
        // endregion

        return countNodesInLayer(index) * countNodesInLayer(index + 1);
    }

    /**
     * Returns a copy of the weights leaving a particular node. For example an layer of 0 will return the weights
     * between the input layer and the first hidden layer.
     *
     * @param layer The layer of the layer of weights
     * @param index The index of the element on the layer
     * @return A copy of the weights connecting one layer to the next.
     */
    public Double[] getWeightsToNode(int layer, int index) {

        // region Argument checks
        if (layer < 1 || layer > getOutputLayerIndex())
            throw new IndexOutOfBoundsException();
        // endregion

        int weightsPerNode = countWeightsInLayer(layer - 1) / countNodesInLayer(layer);
        int start = getLayerStartIndex(layer) + weightsPerNode * index;

        return Arrays.copyOfRange(weights, start, start + weightsPerNode);
    }

    private int getLayerStartIndex(int layer) {
        int layerStart = 0;

        for (int i = 2; i <= layer; i++) {
            layerStart += countWeightsInLayer(i - 2);
        }

        return layerStart;
    }

    private int getNumberOfLayers() {
        return getOutputLayerIndex() + 1;
    }
}
