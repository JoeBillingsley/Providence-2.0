package neural_network;

/**
 * Implementation of a linear step function. Transforms the input excitement value using the logistic function.
 * <p>
 * Created by Joseph Billingsley on 26/10/2015.
 */
public class LogisticStepFunc implements IStepFunction {

    /**
     * @param input The cumulative excitement from all connected nodes.
     * @return An excitement value between 0 and 1.
     */
    @Override
    public double response(double input) {
        return 1 / (1 + Math.pow(Math.E, -1 * input));
    }
}
