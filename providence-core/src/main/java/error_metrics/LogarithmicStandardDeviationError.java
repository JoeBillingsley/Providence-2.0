package error_metrics;

import utils.Variance;

/**
 * Calculates the logarithmic standard deviation error over the estimated efforts and the actual efforts.
 * <p>
 * Created by Joseph Billingsley on 26/11/2015.
 */
public class LogarithmicStandardDeviationError extends ErrorMetric {
    @Override
    public String getName() {
        return "LSD";
    }

    @Override
    public double error(Double[] actuals, Double[] estimates) {
        checkArgumentsLength(actuals, estimates);

        double length = actuals.length;

        Double[] errors = new Double[(int) length];

        for (int i = 0; i < length; i++) {
            double actual = actuals[i];
            double estimate = estimates[i];

            errors[i] = calculateLSDError(actual, estimate);
        }

        double variance = Variance.calculateVariance(errors);

        double sum = 0;

        for (int j = 0; j < length; j++) {
            sum += Math.pow(errors[j] + (variance / 2), 2);
        }

        return Math.sqrt(sum / length);
    }

    @Override
    public boolean isMinimising() {
        return true;
    }

    private double calculateLSDError(Double actual, Double estimate) {
        actual = actual <= 0 ? 1 : actual;
        estimate = estimate <= 0 ? 1 : estimate;

        return Math.log(actual) - Math.log(estimate);
    }

    @Override
    public Double getUpperBound() {
        return 5.0;
    }

    @Override
    public Double getLowerBound() {
        return 0.0;
    }

}
