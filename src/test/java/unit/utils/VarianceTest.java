package unit.utils;

import org.junit.Test;
import utils.Variance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static test_helper.ThrowableCaptor.captureThrowable;

public class VarianceTest {

    @Test
    public void testCalculateVariance() {
        // Arrange
        Double[] a = {1.0, 4.1, 2.5, 3.4, 7.42};
        Double[] b = {};

        // Act
        double aOut = Variance.calculateVariance(a);

        Throwable zeroLength = captureThrowable(() -> Variance.calculateVariance(b));

        // Assert
        assertEquals(4.56, aOut, .01);

        assertNotNull(zeroLength);
        assertEquals(ArithmeticException.class, zeroLength.getClass());
    }

    @Test
    public void testCalculateStandardDeviation() {
        // Arrange
        Double[] a = {1.0, 2.0, 3.0, 4.0, 5.0};
        Double[] b = {};

        // Act
        double aOut = Variance.calculateStandardDeviation(a);

        Throwable zeroLength = captureThrowable(() -> Variance.calculateStandardDeviation(b));

        // Assert
        assertEquals(1.41, aOut, .01);

        assertNotNull(zeroLength);
        assertEquals(ArithmeticException.class, zeroLength.getClass());
    }
}