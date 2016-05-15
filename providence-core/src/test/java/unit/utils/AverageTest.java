package unit.utils;

import org.junit.Test;
import utils.Average;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static test_helper.ThrowableCaptor.captureThrowable;

public class AverageTest {

    @Test
    public void testMean() throws Exception {
        // Arrange
        // Act
        Double normal = Average.Mean(1.0, 2.0, 3.0);
        Double negative = Average.Mean(-3.0, -2.0, -1.0);
        Double decimal = Average.Mean(.5, 1.0, 1.5);

        Throwable throwable = captureThrowable(Average::Mean);

        // Assert
        assertEquals(2, normal, 0);
        assertEquals(-2.0, negative, 0);
        assertEquals(1.0, decimal, 0);

        assertNotNull(throwable);
        assertEquals(IllegalArgumentException.class, throwable.getClass());
    }
}