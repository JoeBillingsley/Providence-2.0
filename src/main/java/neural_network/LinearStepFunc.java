package neural_network;

/**
 * Implementation of a linear step function. Transforms an input excitement value linearly.
 * <p>
 * Created by Joseph Billingsley on 16/11/2015.
 */
public class LinearStepFunc implements IStepFunction {

    /**
     * @param input The cumulative excitement from all connected nodes.
     * @return The same excitement value input into the function.
     */
    @Override
    public double response(double input) {
        return input;
    }

}
