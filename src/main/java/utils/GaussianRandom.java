package utils;

import java.util.Random;

/**
 * a random number generator with a Gaussian probability distribution.
 * <p>
 * Created by Joseph Billingsley on 06/11/2015.
 * Derived from the JMetal implementation in ExtendedPseudoRandomGenerator.java
 */
public class GaussianRandom {

    private static GaussianRandom random;

    private GaussianRandom() {
    }

    /**
     * @return An instance of the
     */
    public static GaussianRandom getInstance() {
        if (random == null)
            random = new GaussianRandom();

        return random;
    }

    /**
     * Produces a random number between 0 and 1 from the distribution centered around the mean with the provided standard
     * deviation.
     *
     * @param mean              The mean of the distribution.
     * @param standardDeviation The standard deviation of the distribution to use.
     * @return A random number between 0 and 1.
     */
    public double nextDouble(double mean, double standardDeviation) {

        Random random = new Random();

        double x1, x2, w, y1;

        do {
            x1 = 2.0 * random.nextDouble() - 1.0;
            x2 = 2.0 * random.nextDouble() - 1.0;
            w = x1 * x1 + x2 * x2;
        } while (w >= 1.0);

        w = Math.sqrt((-2.0 * Math.log(w)) / w);
        y1 = x1 * w;
        y1 = y1 * standardDeviation + mean;

        return y1;
    }

}
