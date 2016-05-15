package evolve_nn;

import org.uma.jmetal.util.solutionattribute.DensityEstimator;
import org.uma.jmetal.util.solutionattribute.impl.GenericSolutionAttribute;
import utils.Distance;
import utils.KNearest;

import java.util.List;

/**
 * Contains methods for calculating the Harmonic Crowding Distance between solutions. Unlike the basic crowding distance
 * calculation this method reduces the effect of outliers when calculating the crowding distance. If one of the distances
 * is especially large whilst the others are still small the calculated crowding distance will still be small.
 * </p>
 * Created by Joseph Billingsley on 08/02/2016.
 * Algorithm from Multi-Objective Approaches to Optimal Testing Resource Allocation in Modular Software Systems
 */
public class HarmonicCrowdingDistance extends GenericSolutionAttribute<SEESolution, Double> implements DensityEstimator<SEESolution> {

    private final int k;
    private List<SEESolution> searchPopulation;

    /**
     * @param k The number of solutions to find the distance to when calculating the crowding distance of a solution.
     */
    public HarmonicCrowdingDistance(int k) {
        // region Argument checks
        if (k < 1)
            throw new IllegalArgumentException("k must be greater than 0");
        // endregion

        this.k = k;
    }

    /**
     * Sets the population that should be searched when looking for the nearest solutions to a particular solution.
     *
     * @param searchPopulation The population that will be searched.
     */
    public void setSearchPopulation(List<SEESolution> searchPopulation) {
        this.searchPopulation = searchPopulation;
    }

    /**
     * Calculates the Harmonic Crowding Distance for the provided solutions. The solutions are annotated with an
     * attribute that contains their respective crowding distance which can be accessed using this classes attributeId.
     *
     * @param solutionSet The solutions to calculate the harmonic crowding distance over.
     */
    @Override
    public void computeDensityEstimator(List<SEESolution> solutionSet) {
        // region Argument checks
        if (solutionSet.size() == 0)
            return;
        // endregion

        for (SEESolution currSolution : solutionSet) {
            List<SEESolution> kNearest = KNearest.find(currSolution, searchPopulation, k);

            double denominator = 0;
            for (SEESolution nearSolution : kNearest) {
                Double distance =
                        Distance.getEuclideanDistance(
                                currSolution.getObjectives(),
                                nearSolution.getObjectives());

                denominator += 1 / distance;
            }

            setAttribute(currSolution, k / denominator);
        }
    }
}
