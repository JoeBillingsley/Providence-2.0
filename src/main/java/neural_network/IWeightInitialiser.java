package neural_network;

/**
 * Implementations of this class have methods that provide initial weights for a neural network.
 * <p>
 * Created by Joseph Billingsley on 28/10/2015.
 */
public interface IWeightInitialiser {

    /**
     * Provides the initial weights for a neural network.
     *
     * @param weightCount The number of weights to produced
     * @param parameters  Any parameters the implementation requires.
     * @return The initial weights.
     */
    Double[] init(int weightCount, Object... parameters);
}
