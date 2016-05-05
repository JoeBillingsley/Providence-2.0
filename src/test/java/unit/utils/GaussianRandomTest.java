package unit.utils;

import org.junit.Test;
import utils.GaussianRandom;

import static org.junit.Assert.assertEquals;

public class GaussianRandomTest {

    @Test
    public void testGetInstance() {
        // Act
        GaussianRandom instanceOne = GaussianRandom.getInstance();
        GaussianRandom instanceTwo = GaussianRandom.getInstance();

        // Assert
        assertEquals(instanceOne, instanceTwo);
    }

    @Test
    public void testNextDouble() {
        // Arrange
        GaussianRandom instance = GaussianRandom.getInstance();

        // Act
        instance.nextDouble(10, 5);
        instance.nextDouble(0, 0);
        instance.nextDouble(-20, -3);

        // Assert
        // All is well if no exceptions are thrown.
    }
}