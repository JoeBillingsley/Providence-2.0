package clustering;

import java.util.List;

/**
 * Classes that extends this interface are able to initialise the means of clusters for the K-Means clustering
 * algorithm.
 * <p>
 * Created by Joseph Billingsley on 06/01/2016.
 */
public interface IMeanInitialiser {
    /**
     * @param k          The number of means to initialise.
     * @param dataPoints The source points that will be clustered.
     * @return The initial means to start the K Means clustering algorithm with.
     */
    List<Double[]> initialiseMeans(int k, List<Double[]> dataPoints);
}
