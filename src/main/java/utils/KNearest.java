package utils;

import evolve_nn.SEESolution;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for finding the K nearest solutions to a solution.
 * <p>
 * Created by Joseph Billingsley on 08/02/2016.
 */
public class KNearest {

    /**
     * Finds the k-nearest solutions in euclidean space based on the objective values of each solution.
     *
     * @param solution              The solution to find the nearest neighbours for.
     * @param neighbouringSolutions The set of neighbouring solutions.
     * @param k                     The number of nearest neighbours to look for.
     * @return The k-nearest neighbouring solutions.
     */
    @NotNull
    public static List<SEESolution> find(SEESolution solution, List<SEESolution> neighbouringSolutions, int k) {

        List<Double> kNearestDistances = new ArrayList<>();
        List<SEESolution> kNearestSolutions = new ArrayList<>();

        for (SEESolution neighbouringSolution : neighbouringSolutions) {

            if (solution == neighbouringSolution)
                continue;

            Double distance = Distance.getEuclideanDistance(solution.getObjectives(), neighbouringSolution.getObjectives());

            // Insertion sort
            boolean fit = false;

            for (int i = 0; i < kNearestSolutions.size(); i++) {
                if (distance < kNearestDistances.get(i)) {
                    kNearestDistances.add(i, distance);
                    kNearestSolutions.add(i, neighbouringSolution);

                    fit = true;
                    break;
                }
            }

            if (fit) continue;

            kNearestSolutions.add(neighbouringSolution);
            kNearestDistances.add(distance);
        }

        if (kNearestSolutions.size() <= k)
            return kNearestSolutions;

        return kNearestSolutions.subList(0, k);
    }
}
