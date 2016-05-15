package clustering;

import utils.Distance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides utilities for reducing a collection of points into a population of k representative means.
 * <p>
 * Algorithm: https://en.wikipedia.org/wiki/K-means_clustering
 * Created by Joseph Billingsley on 04/01/2016.
 */
public class KMeansClustering {

    private final List<Double[]> points;
    private IMeanInitialiser initialiser;

    public KMeansClustering(List<Double[]> points, IMeanInitialiser initialiser) {
        this.points = points;
        this.initialiser = initialiser;
    }

    /**
     * Partitions the provided points into k clusters.
     *
     * @param k The number of clusters to divide the points into.
     */
    public Cluster[] run(int k) {

        Cluster[] clusters = new Cluster[k];
        for (int i = 0; i < clusters.length; i++) {
            clusters[i] = new Cluster();
        }

        List<Double[]> prevMeans;
        List<Double[]> means = initialiser.initialiseMeans(k, points);

        do {
            prevMeans = means;
            clusters = assignmentStep(clusters, means, points);
            means = updateStep(clusters);
        } while (!areConverged(means, prevMeans));

        return clusters;
    }

    private boolean areConverged(List<Double[]> means, List<Double[]> prevMeans) {
        for (int i = 0; i < means.size(); i++) {

            Double[] mean = means.get(i);
            Double[] prevMean = prevMeans.get(i);

            if (!Arrays.deepEquals(mean, prevMean))
                return false;
        }

        return true;
    }


    private Cluster[] assignmentStep(Cluster[] clusters, List<Double[]> means, List<Double[]> points) {

        // Remove any points in a cluster
        for (Cluster cluster : clusters) {
            cluster.clear();
        }

        // Assign each point to the closest mean
        for (Double[] point : points) {

            int closestCluster = 0;
            double distanceToClosestMean = Double.MAX_VALUE;

            for (int j = 0; j < means.size(); j++) {
                Double distance = Distance.getEuclideanDistance(point, means.get(j));

                if (distance < distanceToClosestMean) {
                    closestCluster = j;
                    distanceToClosestMean = distance;
                }
            }

            clusters[closestCluster].addPoints(point);
        }

        return clusters;
    }

    private List<Double[]> updateStep(Cluster[] clusters) {
        List<Double[]> means = new ArrayList<>();

        for (Cluster cluster : clusters) {
            if (cluster.getPoints().size() > 0) {
                means.add(cluster.getMean());
            }
        }

        return means;
    }
}
