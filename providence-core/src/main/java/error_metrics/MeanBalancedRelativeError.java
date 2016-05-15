package error_metrics;

/**
 * Calculates the balanced mean magnitude relative error over the estimated efforts and the actual efforts.
 * <p>
 * Created by Joseph Billingsley on 03/04/2016.
 */
public class MeanBalancedRelativeError extends MeanError {
    public MeanBalancedRelativeError() {
        super((actual, prediction) -> {
            double diff = prediction - actual;

            double val = 0;

            if (diff >= 0)
                val = diff / actual;
            else
                val = diff / prediction;

            return Math.abs(val);
        });
    }

    @Override
    public boolean isMinimising() {
        return true;
    }

    @Override
    public String getName() {
        return "MBRE";
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
