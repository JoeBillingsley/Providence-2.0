package neural_network;

/**
 * Interface for functions that transform an input excitement value into an output.
 *
 * Created by Joseph Billingsley on 26/10/2015.
 */
public interface IStepFunction {

    /**
     * @param input The cumulative excitement from all connected nodes.
     * @return The new excitement level
     */
    double response(double input);

}
