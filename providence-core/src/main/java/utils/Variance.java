package utils;

/**
 * Helper class for calculating the population variance and standard deviance of a set of numbers.
 * <p>
 * Created by Joseph Billingsley on 21/02/2016.
 */
public class Variance {

    /**
     * Calculates the population variance of the provided numbers.
     *
     * @param numbers The population to calculate the variance of.
     * @return The variance of the population.
     */
    public static double calculateVariance(Double[] numbers) {
        int length = numbers.length;

        if (length == 0)
            throw new ArithmeticException();

        Double mean = Average.Mean(numbers);

        Double squaredDifference = 0.0;
        for (Double number : numbers) {
            squaredDifference += Math.pow(number - mean, 2);
        }

        return squaredDifference / length;
    }

    /**
     * Calculates the population standard deviation of the provided numbers.
     *
     * @param numbers The population to calculate the standard deviation of.
     * @return The standard deviation of the population.
     */
    public static double calculateStandardDeviation(Double[] numbers) {
        return Math.sqrt(calculateVariance(numbers));
    }
}
