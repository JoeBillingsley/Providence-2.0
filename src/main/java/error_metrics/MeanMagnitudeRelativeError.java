package error_metrics;

/**
 * Calculates the mean magnitude relative error over the estimated efforts and the actual efforts.
 * <p>
 * Created by Joseph Billingsley on 26/11/2015.
 */
public class MeanMagnitudeRelativeError extends MeanError {

    public MeanMagnitudeRelativeError() {
        super(MRE::calculate);
    }

    @Override
    public String getName() {
        return "MMRE";
    }

    @Override
    public boolean isMinimising() {
        return true;
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
