package error_metrics;

import custom_controls.IAxis;

/**
 * Common class of all classes that can be used by the system to calculate the error between the actual and estimated
 * efforts.
 * <p>
 * Created by Joseph Billingsley on 02/11/2015.
 */
public abstract class ErrorMetric implements IAxis {

    /**
     * Calculates an implementation specific distance between the estimate and actual efforts.
     *
     * @param actuals   The actual efforts.
     * @param estimates The estimated efforts.
     * @return The difference of the actual and estimate effort.
     */
    public abstract double error(Double[] actuals, Double[] estimates);

    /**
     * @return True the error metric will produce values closer to zero as the estimate and actual efforts get closer
     * together, false otherwise.
     */
    public abstract boolean isMinimising();

    protected void checkArgumentsLength(Double[] actuals, Double[] estimates) {
        if (actuals.length != estimates.length)
            throw new IllegalArgumentException(
                    "The number of actual and predicted elements is not the same"
                            + ": actual " + actuals.length
                            + ", prediction " + estimates.length);
    }

    @Override
    public String toString() {
        return getName();
    }
}
