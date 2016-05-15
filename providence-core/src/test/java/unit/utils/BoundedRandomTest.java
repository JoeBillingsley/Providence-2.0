package unit.utils;

import org.junit.Test;
import utils.BoundedRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BoundedRandomTest {

    @Test
    public void testNextDouble() throws Exception {

        // Arrange
        // Act
        Double positive = BoundedRandom.nextDouble(0, 50);
        Double negative = BoundedRandom.nextDouble(-51, -1);
        Double certain = BoundedRandom.nextDouble(1, 1);
        Double crossed = BoundedRandom.nextDouble(1, -1);

        // Assert
        assertTrue(positive > 0);
        assertTrue(negative < 0);
        assertEquals(1.0, certain, 0);

        assertTrue(crossed < 1);
        assertTrue(crossed > -1);
    }
}