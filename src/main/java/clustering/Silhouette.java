package clustering;

import org.jetbrains.annotations.Contract;
import utils.Distance;

/**
 * Provides utilities for finding the silhouette value of points in a cluster which represents how well a point is suited
 * to a particular cluster compared to other potential clusters.
 * <p>
 * Created by Joseph Billingsley on 04/01/2016.
 * Algorithm: https://en.wikipedia.org/wiki/Silhouette_%28clustering%29
 */
public class Silhouette {

    /**
     * Determines the silhouette of a provided point with respect to the clusters it belongs to and other clusters
     * derived from the same data set. The value represents the similarity of a point in a cluster to other points in
     * it's cluster. The point cluster must not contain the point being tested.
     *
     * @param point         The point to determine the silhouette of.
     * @param pointCluster  The cluster the provided point belongs to.
     * @param otherClusters The other clusters the point does not belong to.
     * @return The silhouette value of the provided point.
     */
    @Contract("_, _, null -> fail")
    public static double getSilhouetteValue(Double[] point, Cluster pointCluster, Cluster... otherClusters) {
        // region Argument checks
        if (otherClusters == null || otherClusters.length == 0)
            throw new IllegalArgumentException("Silhouette calculation requires at least two cluster");
        // endregion

        double averageDissimilarity = averageDissimilarity(point, pointCluster);
        double lowestAverageDissimilarity = lowestAverageDissimilarity(point, otherClusters);

        return (lowestAverageDissimilarity - averageDissimilarity)
                / (Double.max(averageDissimilarity, lowestAverageDissimilarity));
    }

    private static double averageDissimilarity(Double[] point, Cluster cluster) {
        double sum = 0;

        for (Double[] clusterPoint : cluster.getPoints()) {
            sum += Distance.getEuclideanDistance(point, clusterPoint);
        }

        return sum / cluster.getPoints().size();
    }

    private static double lowestAverageDissimilarity(Double[] point, Cluster[] clusters) {

        double minAverageDissimilarity = Double.MAX_VALUE;

        for (Cluster cluster : clusters) {
            Double currDissimilarity = averageDissimilarity(point, cluster);

            if (currDissimilarity < minAverageDissimilarity) {
                minAverageDissimilarity = currDissimilarity;
            }
        }

        return minAverageDissimilarity;
    }

}
