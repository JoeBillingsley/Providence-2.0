package utils;

import org.jetbrains.annotations.NotNull;

/**
 * Helper class for calculating the distance between two points. The points must have one or more dimensions and have the
 * same number of dimensions but otherwise no restrictions are applied.
 * <p>
 * Created by Joseph Billingsley on 02/01/2016.
 */
public class Distance {

    /**
     * Calculates the straight line Euclidean distance between two points. Both points must have the same number of
     * dimensions.
     *
     * @param x A point in 1 or more dimensional space.
     * @param y A point in 1 or more dimensional space.
     * @return The Euclidean distance between the two points.
     */
    @NotNull
    public static Double getEuclideanDistance(Double[] x, Double[] y) {

        // region Argument checks
        if (x.length != y.length)
            throw new IllegalArgumentException(
                    "The points being compared must have the same number of dimensions: X:" + x.length + ", Y:" + y.length);
        // endregion

        double distance = 0;

        for (int i = 0; i < x.length; i++) {
            double diff = x[i] - y[i];
            distance += Math.pow(diff, 2);
        }

        return Math.sqrt(distance);
    }

}
