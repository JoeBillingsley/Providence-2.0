package clustering;

import dataset.Project;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides utilities for removing outliers from a given population.
 * <p>
 * Created by Joseph Billingsley on 15/02/2016.
 */
public class OutlierDetector {

    private final IClustering clustering;
    private int lowerBound, upperBound;

    public OutlierDetector(IClustering clustering) {
        this(clustering, 2, 5);
    }

    public OutlierDetector(IClustering clustering, int lowerBound, int upperBound) {
        this.clustering = clustering;

        setLowerBound(lowerBound);
        setUpperBound(upperBound);
    }

    /**
     * Identifies points that are not representative of the rest of the population and returns them.
     *
     * @param points The points to search for outliers amongst.
     * @return The points which were found to be outliers.
     */
    public List<Project> getOutliers(List<Project> points) {

        if (upperBound > points.size())
            upperBound = points.size();

        Cluster[] clusters = findBestClustering(points, lowerBound, upperBound);

        // Next detect any outliers
        // - Any cluster with less than n points
        // - Any point with a negative silhouette

        int n = 3;

        List<Project> outliers = new ArrayList<>();

        for (Cluster cluster : clusters) {
            if (cluster.getPoints().size() < n) {
                outliers.addAll(cluster.getPoints());
                continue;
            }

            // TODO LOGICAL ERROR - Removing too many clusters
            Cluster[] otherClusters = ArrayUtils.removeElement(clusters, cluster);

            outliers.addAll(cluster.getPoints().stream()
                    .filter(point -> Silhouette.getSilhouetteValue(point, cluster, otherClusters) < 0)
                    .collect(Collectors.toList()));
        }

        return outliers;
    }

    /**
     * Given a range of possible numbers of clusters, finds the arrangement of clusters that maximises the average
     * silhouette value of all points.
     *
     * @param points     The points that will be clustered.
     * @param lowerBound The lower bound of the search. Must be 2 or more.
     * @param upperBound The upper bound of the search. Must not be less than the lower bound.
     * @return An arrangement of clusters that maximises the average silhouette value.
     * @throws IllegalArgumentException If the lower bound is less than 2.
     * @throws IllegalArgumentException If the upper bound is less than the lower bound.
     * @throws IllegalArgumentException If the upper bound is more than the highest index of points.
     */
    private Cluster[] findBestClustering(List<Project> points, int lowerBound, int upperBound) {

        // region Argument checks
        if (lowerBound < 2)
            throw new IllegalArgumentException("The lower bound cannot be less than 2: " + lowerBound);

        if (upperBound < lowerBound)
            throw new IllegalArgumentException(
                    "The upper bound cannot be less than the lower bound. Lower bound:" + lowerBound + ", upper bound:" + upperBound);

        if (upperBound > points.size())
            throw new ArrayIndexOutOfBoundsException(
                    "The upper bound cannot be more than the highest index of points. Upper bound: " + upperBound + ", number of points: " + points.size());
        // endregion

        Cluster[] bestClustering = null;

        double maxAvgSVal = Double.MIN_VALUE;

        for (int k = lowerBound; k <= upperBound; k++) {
            Cluster[] clusters = clustering.run(points, k);

            double totalSVal = 0;

            for (Cluster cluster : clusters) {
                Cluster[] otherClusters = ArrayUtils.removeElement(clusters, cluster);

                if (cluster.getPoints().size() == 0)
                    continue;

                for (Project point : cluster.getPoints()) {
                    totalSVal += Silhouette.getSilhouetteValue(point, cluster, otherClusters);
                }
            }

            double avgSVal = totalSVal / points.size();

            if (avgSVal > maxAvgSVal) {
                maxAvgSVal = avgSVal;
                bestClustering = clusters;
            }
        }

        return bestClustering;
    }

    public void setLowerBound(int lowerBound) {
        this.lowerBound = lowerBound;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }
}
