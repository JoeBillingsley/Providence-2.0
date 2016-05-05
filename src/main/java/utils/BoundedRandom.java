package utils;

import java.util.Random;

/**
 * Helper class for finding random numbers in a range.
 * <p>
 * Created by Joseph Billingsley on 05/01/2016.
 */
public class BoundedRandom {

    private static Random rand = new Random();

    /**
     * Generates a random number in the provided range inclusive of the upper and lower bound.
     *
     * @param lowerBound The lowest number that could be returned.
     * @param upperBound The highest number that could be returned
     * @return A number in the range of the lower and upper bound inclusive.
     */
    public static double nextDouble(double lowerBound, double upperBound) {
        return lowerBound + (upperBound - lowerBound) * rand.nextDouble();
    }

    public static int nextInt(int lowerBound, int upperBound) {
        return lowerBound + rand.nextInt(upperBound - lowerBound + 1);
    }
}
