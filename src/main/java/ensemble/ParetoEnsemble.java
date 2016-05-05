package ensemble;

import evolve_nn.SEESolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an ensemble consisting of the solutions with the lowest error solutions in each metric.
 * <p>
 * Created by Joseph Billingsley on 23/12/2015.
 */
public class ParetoEnsemble implements IEnsemble {

    private List<SEESolution> winningCandidates;

    /**
     * Produces an ensemble of solutions consisting of the solution with the lowest error in each metric from the
     * candidate solutions. The size of the ensemble is the same as the number of error metrics.
     *
     * @param candidates The solutions from which the ensemble will be constructed out of.
     */
    public ParetoEnsemble(List<SEESolution> candidates) {

        winningCandidates = new ArrayList<>();

        int numberOfObjectives = candidates.get(0).getNumberOfObjectives();

        for (int i = 0; i < numberOfObjectives; i++) {

            SEESolution currentBestCandidate = candidates.get(0);

            for (SEESolution candidate : candidates) {
                if (candidate.getObjective(i) < currentBestCandidate.getObjective(i)) {
                    currentBestCandidate = candidate;
                }
            }

            winningCandidates.add(currentBestCandidate);
        }
    }

    /**
     * Gets the mean result of the estimates produced by each solution in the ensemble.
     *
     * @param inputs The data to provide to each solution.
     * @return The combined estimate.
     */
    public Double getResult(Double[] inputs) {

        double sum = 0;
        for (SEESolution solution : winningCandidates) {
            sum += solution.getNeuralNetwork().executeNetwork(inputs)[0];
        }

        return sum / winningCandidates.size();
    }
}
