package unit.utils;

import org.junit.Test;
import utils.Distance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static test_helper.ThrowableCaptor.captureThrowable;

public class DistanceTest {

    @Test
    public void testGetDistance() throws Exception {

        // Arrange
        Double[] x = {1.0, 1.0, 1.0};
        Double[] y = {1.0, 2.0, 3.0};

        Double[] smallY = {3.0, 3.0};

        // Act
        double distance = Distance.getEuclideanDistance(x, y);
        Throwable error = captureThrowable(() -> Distance.getEuclideanDistance(x, smallY));

        // Assert
        assertEquals(distance, 2.236, .001);

        assertNotNull(error);
        assertEquals(IllegalArgumentException.class, error.getClass());
    }
}