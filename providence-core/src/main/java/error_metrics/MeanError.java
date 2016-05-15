package error_metrics;

import java.util.function.BiFunction;

/**
 * Represents error metrics that are based around calculating the mean of a series of errors.
 * <p>
 * Created by Joseph Billingsley on 21/03/2016.
 */
public abstract class MeanError extends ErrorMetric {
    private BiFunction<Double, Double, Double> error;

    /**
     * Creates a mean error with the provided error function.
     *
     * @param error A function that calculates the error between the actual and estimated errors.
     */
    public MeanError(BiFunction<Double, Double, Double> error) {
        this.error = error;
    }

    @Override
    public double error(Double[] actuals, Double[] estimates) {
        // region Argument checks
        checkArgumentsLength(actuals, estimates);

        if(estimates.length == 0)
            throw new IllegalArgumentException("There must exist at least one actual and estimated effort.");
        // endregion

        double length = actuals.length;
        double sum = 0;

        for (int i = 0; i < length; i++) {

            double actual = actuals[i];
            double prediction = estimates[i];

            sum += error.apply(actual, prediction);
        }

        return sum / length;
    }
}
