package error_metrics;

/**
 * Calculates the percentage of estimates that are within 25% of the actual effort.
 * <p>
 * Created by Joseph Billingsley on 26/11/2015.
 */
public class PRED25Error extends MeanError {

    public PRED25Error() {
        super((actual, prediction) -> {
            double mre = MRE.calculate(actual, prediction);

            if (mre <= .25)
                return 1.0;

            return 0.0;
        });
    }

    @Override
    public String getName() {
        return "PRED25";
    }

    @Override
    public boolean isMinimising() {
        return false;
    }

    @Override
    public Double getUpperBound() {
        return 1.0;
    }

    @Override
    public Double getLowerBound() {
        return 0.0;
    }

}
