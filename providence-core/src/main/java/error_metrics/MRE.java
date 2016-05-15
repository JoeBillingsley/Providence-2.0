package error_metrics;

/**
 * Provides utilities to calculate the mean relative error of a prediction.
 * <p>
 * Created by Joseph Billingsley on 26/11/2015.
 */
public class MRE {

    /**
     * Calculates the mean relative error of a prediction when compared to the actual effort.
     *
     * @param actual     The actual effort
     * @param prediction The predicted effort
     * @return The mean relative error.
     */
    public static double calculate(double actual, double prediction) {
        double diff = Math.abs(actual - prediction);
        if (actual == 0) actual = 1;
        return diff / actual;
    }
}
