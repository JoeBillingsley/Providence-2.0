package error_metrics;

/**
 * Calculates the inverted balanced mean magnitude relative error over the estimated efforts and the actual efforts.
 * <p>
 * Created by Joseph Billingsley on 03/04/2016.
 */
public class MeanInvertedBalancedRelativeError extends MeanError {

    public MeanInvertedBalancedRelativeError() {
        super((actual, prediction) -> {
            double diff = prediction - actual;

            double val = 0;

            if (diff >= 0)
                val = diff / prediction;
            else
                val = diff / actual;

            return Math.abs(val);
        });
    }

    @Override
    public boolean isMinimising() {
        return true;
    }

    @Override
    public String getName() {
        return "MIBRE";
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
