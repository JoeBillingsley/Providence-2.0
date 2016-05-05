package clustering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a group of points that can be considered spatially connected.
 * <p>
 * Created by Joseph Billingsley on 06/01/2016.
 */
public class Cluster {

    private ArrayList<Double[]> points = new ArrayList<>();

    /**
     * Stores the provided points in the cluster.
     *
     * @param points The points to add into the cluster.
     */
    public void addPoints(Double[]... points) {
        Collections.addAll(this.points, points);
    }

    /**
     * @return The points stored in the cluster.
     */
    public List<Double[]> getPoints() {
        return points;
    }

    /**
     * @return The mean of the points stored in the cluster or null if there are no points in the cluster.
     */
    public Double[] getMean() {
        if (points.size() == 0)
            throw new IllegalArgumentException("There must be points in the cluster to calculate the mean.");

        Double[] mean = new Double[points.get(0).length];

        for (int i = 0; i < mean.length; i++) {
            mean[i] = 0.0;
        }

        for (int i = 0; i < mean.length; i++) {
            for (Double[] point : points) {
                mean[i] += point[i];
            }

            mean[i] = mean[i] / points.size();
        }

        return mean;
    }

    /**
     * Removes all points from the cluster.
     */
    public void clear() {
        points.clear();
    }
}
