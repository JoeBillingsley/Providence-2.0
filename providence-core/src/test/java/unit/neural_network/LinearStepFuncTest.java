package unit.neural_network;

import neural_network.IStepFunction;
import neural_network.LinearStepFunc;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LinearStepFuncTest {

    @Test
    public void testResponse() {
        // Arrange
        IStepFunction linear = new LinearStepFunc();

        // Act
        double negative = linear.response(-1);
        double zero = linear.response(0);
        double one = linear.response(1);
        double two = linear.response(2);
        double three = linear.response(3);

        // Assert
        assertEquals(zero - negative, one - zero, 0);
        assertEquals(one -  zero, two - one, 0);
        assertEquals(two - one, three - two, 0);
        assertEquals(three - one, one - negative, 0);
    }

}