package unit.neural_network;

import neural_network.IStepFunction;
import neural_network.LogisticStepFunc;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LogisticStepFuncTest {

    @Test
    public void testResponse() {
        // Arrange
        IStepFunction logarithmic = new LogisticStepFunc();

        // Act
        double positive = logarithmic.response(1);
        double largePositive = logarithmic.response(30);
        double zero = logarithmic.response(0);
        double negative = logarithmic.response(-1);
        double largeNegative = logarithmic.response(-30);

        // Assert
        assertEquals(positive, 0.73, 0.01);
        assertEquals(largePositive, 0.99, 0.01);
        assertEquals(zero, 0.5, 0);
        assertEquals(negative, 0.26, 0.01);
        assertEquals(largeNegative, 0.01, 0.01);
    }
}