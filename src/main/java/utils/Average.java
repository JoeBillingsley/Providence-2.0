package utils;

import org.jetbrains.annotations.NotNull;

/**
 * Helper class for calculating the mean of a list of numbers.
 * <p>
 * Created by Joseph Billingsley on 22/02/2016.
 */
public class Average {

    /**
     * Calculates the mean of the provided collection of numbers.
     *
     * @param numbers The numbers to calculate the mean of
     * @return The mean of the numbers.
     */
    @NotNull
    public static Double Mean(Double... numbers) {
        if (numbers.length == 0)
            throw new IllegalArgumentException("There must be one or more parameters");

        Double sum = 0.0;

        for (Double number : numbers) {
            sum += number;
        }

        return sum / numbers.length;
    }

}
