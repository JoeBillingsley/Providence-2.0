package utils;

import org.jetbrains.annotations.NotNull;

/**
 * Provides utilities for normalising value.
 */
public class Normalise {

    /**
     * Scales a feature to between 0 and 1 when given the upper and lower bounds the value could take.
     *
     * @param value      The value to scale.
     * @param lowerBound The smallest value the feature could take.
     * @param upperBound The highest value the feature could take.
     * @return The value of the feature when scaled to be between 0 and 1.
     */
    @NotNull
    public static Double scaleFeature(Double value, Double lowerBound, Double upperBound) {
        if (lowerBound > upperBound)
            throw new IllegalArgumentException("The lower bound is greater than the upper bound."
                    + " Lower bound: " + lowerBound
                    + " Upper bound: " + upperBound);

        return (value - lowerBound) / (upperBound - lowerBound);
    }

}
