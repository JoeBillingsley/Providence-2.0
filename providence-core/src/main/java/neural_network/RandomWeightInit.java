package neural_network;

import utils.BoundedRandom;

/**
 * Provides functionality for initialising the weights for a neural network randomly within some bounds.
 * <p>
 * Created by Joseph Billingsley on 28/10/2015.
 */
public class RandomWeightInit implements IWeightInitialiser {

    /**
     * Initialises an array of weights for a neural network. Produces random values for the weights between the provided
     * bounds.
     *
     * @param weightCount The number of weights to initialise.
     * @param parameters  parameters[0]:int Lower bound
     *                    parameters[1]:int Upper bound
     */
    @Override
    public Double[] init(int weightCount, Object... parameters) {

        int lowerBound = (int) parameters[0];
        int upperBound = (int) parameters[1];

        Double[] weights = new Double[weightCount];

        for (int i = 0; i < weights.length; i++) {
            weights[i] = BoundedRandom.nextDouble(lowerBound, upperBound);
        }

        return weights;
    }
}
