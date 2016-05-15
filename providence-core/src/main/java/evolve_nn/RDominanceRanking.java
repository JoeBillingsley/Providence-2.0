package evolve_nn;

import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.solutionattribute.Ranking;
import org.uma.jmetal.util.solutionattribute.impl.DominanceRanking;
import org.uma.jmetal.util.solutionattribute.impl.GenericSolutionAttribute;
import utils.Distance;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a means for incorporating preference into an evolutionary algorithm using the r-Dominance ranking algorithm.
 * The distances of all solutions to an aspiration point are calculated using the Euclidean distance. The solutions are
 * then ranked according to the r-Dominance relation. Under r-Dominance a solution X dominates a solution Y if solution X
 * pareto dominates Y or is incomparable but the difference of the distance from X to the aspiration point and Y to
 * the aspiration point is greater than the non r-Dominance threshold.
 * <p>
 * Algorithm from 'The r-Dominance: A New Dominance Relation for Interactive Evolutionary Multicriteria Decision
 * Making'. Lamjed Ben Said, Slim Bechikh and Khaled Ghedira. IEEE Transactions on Evolutionary Computation, Vol. 14
 * October 2010.
 * <p>
 * Created by Joseph Billingsley on 28/12/2015.
 */
public class RDominanceRanking
        extends GenericSolutionAttribute<SEESolution, Integer> implements Ranking<SEESolution> {

    private List<List<SEESolution>> rankedSubpopulations;

    private Double[] aspirationPoint;
    private Double nonRDominanceThreshold;

    /**
     * @param aspirationPoint        The aspiration point to rank solutions around.
     * @param nonRDominanceThreshold The threshold over which one solution can be said to dominate another solution.
     *                               Must be in the range 0 to 1.
     */
    public RDominanceRanking(Double[] aspirationPoint, Double nonRDominanceThreshold) {
        // region Argument checks
        if (nonRDominanceThreshold < 0 || nonRDominanceThreshold > 1)
            throw new IllegalArgumentException("Threshold must be between 0 and 1");
        // endregion

        this.aspirationPoint = aspirationPoint;
        this.rankedSubpopulations = new ArrayList<>();
        this.nonRDominanceThreshold = nonRDominanceThreshold;
    }

    @Override
    public Ranking<SEESolution> computeRanking(List<SEESolution> population) {
        if (aspirationPoint == null) {
            Ranking<SEESolution> ranking = new DominanceRanking<>();
            ranking.computeRanking(population);

            return ranking;
        }

        // First find maximum and minimum distances amongst whole population
        double maxDist = 0, minDist = 0;

        for (SEESolution solution : population) {
            double dist = Distance.getEuclideanDistance(solution.getObjectives(), aspirationPoint);

            if (maxDist < dist) maxDist = dist;
            if (minDist > dist) minDist = dist;
        }

        // Keep track of how many population dominate each solution
        int[] dominateCounter = new int[population.size()];

        // Determine who dominates who
        for (int i = 0; i < population.size(); i++) {
            for (SEESolution solution : population) {
                boolean xRDominatesY = xRDominatesY(solution, population.get(i), maxDist, minDist);

                if (xRDominatesY)
                    dominateCounter[i]++;
            }
        }

        buildFronts(dominateCounter, population);

        return this;
    }

    private void buildFronts(int[] dominateCounter, List<SEESolution> solutions) {
        int start = 0;

        while (start < dominateCounter.length) {
            ArrayList<SEESolution> subpopulation = new ArrayList<>();

            // Find distance to next level
            double nextLevelDistance = Double.MAX_VALUE;

            for (int j = start; j < dominateCounter.length; j++) {
                if (dominateCounter[j] > 0 && dominateCounter[j] < nextLevelDistance) {
                    nextLevelDistance = dominateCounter[j];
                }
            }

            boolean zerosSoFar = true;
            for (int j = start; j < dominateCounter.length; j++) {
                if (dominateCounter[j] == 0) {
                    subpopulation.add(solutions.get(j));
                } else if (dominateCounter[j] > 0) {
                    zerosSoFar = false;
                }

                if (zerosSoFar)
                    start++;

                dominateCounter[j] -= nextLevelDistance;
            }

            rankedSubpopulations.add(subpopulation);
        }
    }

    private boolean xRDominatesY(SEESolution x, SEESolution y, double maxDist, double minDist) {

        DominanceComparator<SEESolution> dominanceComparator = new DominanceComparator<>();
        int paretoDominance = dominanceComparator.compare(x, y);

        if(paretoDominance == 1)
            return false;

        if(paretoDominance == -1)
            return true;

        double distXG = Distance.getEuclideanDistance(x.getObjectives(), aspirationPoint);
        double distYG = Distance.getEuclideanDistance(y.getObjectives(), aspirationPoint);

        double distXYG = (distXG - distYG) / (maxDist - minDist);

        return distXYG < -nonRDominanceThreshold;
    }

    @Override
    public List<SEESolution> getSubfront(int rank) {
        return rankedSubpopulations.get(rank);
    }

    @Override
    public int getNumberOfSubfronts() {
        return rankedSubpopulations.size();
    }
}