package unit.neural_network;

import neural_network.IWeightInitialiser;
import neural_network.RandomWeightInit;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RandomWeightInitTest {

    @Test
    public void testInit() {
        // Arrange
        IWeightInitialiser init = new RandomWeightInit();

        // Act
        Double[] weights = init.init(10, -1, 1);

        // Assert
        assertEquals(weights.length, 10);

        for (Double weight : weights) {
            assertEquals(0, weight, 1);
        }
    }

}