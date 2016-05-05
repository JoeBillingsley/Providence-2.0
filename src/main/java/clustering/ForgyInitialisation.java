package clustering;

import java.util.Collections;
import java.util.List;

/**
 * Implementation of the Forgy method for initialising the means in the K-Means clustering algorithm.
 * <p>
 * Created by Joseph Billingsley on 06/01/2016.
 */
public class ForgyInitialisation implements IMeanInitialiser {
    /**
     * Picks k random different candidate points to use as means.
     *
     * @param k               The number of means to request.
     * @param candidatePoints The points to select means from.
     * @return K means centered on random candidate points.
     * @throws IndexOutOfBoundsException If k is larger than the number of candidate points.
     */
    @Override
    public List<Double[]> initialiseMeans(int k, List<Double[]> candidatePoints) {
        Collections.shuffle(candidatePoints);

        if (k > candidatePoints.size())
            throw new IndexOutOfBoundsException("The number of centers requested is larger than the number of candidate points." +
                    " k: " + k +
                    " points: " + candidatePoints.size());

        return candidatePoints.subList(0, k);

    }
}
