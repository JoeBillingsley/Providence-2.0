package unit.error_metrics;

import error_metrics.MeanMagnitudeRelativeError;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MeanMagnitudeRelativeErrorTest {

    @Test
    public void testError() throws Exception {
        // Arrange
        Double[] actuals = {1.0, 1.0, 1.0}, estimates = {1.0, 2.0, 3.0};
        MeanMagnitudeRelativeError mmre = new MeanMagnitudeRelativeError();

        // Act
        double error = mmre.error(actuals, estimates);

        // Assert
        assertEquals(1.0, error, 0);
    }
}