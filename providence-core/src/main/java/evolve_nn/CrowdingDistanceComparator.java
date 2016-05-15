package evolve_nn;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.solutionattribute.DensityEstimator;

import java.util.Comparator;

/**
 * Comparator for ordering solutions with by crowding distance.
 * <p>
 * Created by Joseph Billingsley on 04/03/2016.
 */
public class CrowdingDistanceComparator<S extends Solution<?>> implements Comparator<S> {

    private final DensityEstimator<S> densityEstimator;

    public CrowdingDistanceComparator(DensityEstimator<S> densityEstimator) {
        this.densityEstimator = densityEstimator;
    }

    @Override
    public int compare(S solutionOne, S solutionTwo) {

        if (solutionOne == null && solutionTwo == null)
            return 0;
        else if (solutionOne == null)
            return 1;
        else if (solutionTwo == null)
            return -1;

        Double densityOne = densityEstimator.getAttribute(solutionOne);
        Double densityTwo = densityEstimator.getAttribute(solutionTwo);

        if (densityOne > densityTwo)
            return -1;

        if (densityOne < densityTwo)
            return 1;

        return 0;
    }
}
