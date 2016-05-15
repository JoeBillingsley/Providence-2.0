package neural_network;

/**
 * A neural network of any categorisation. The interface places no requirements on how the network is built or how it is
 * structured.
 * <p>
 * Created by Joseph Billingsley on 02/11/2015.
 */
public interface INeuralNetwork {

    Double[] executeNetwork(Double[] inputs);

    Double getWeight(int index);

    void setWeight(int index, Double value);

    Double[] getWeights();

    Double[] getThresholds();

    Double getThreshold(int index);

    void setThreshold(int index, Double value);

    INeuralNetwork copy();
}
