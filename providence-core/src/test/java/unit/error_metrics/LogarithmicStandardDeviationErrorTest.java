package unit.error_metrics;

import error_metrics.LogarithmicStandardDeviationError;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LogarithmicStandardDeviationErrorTest {

    @Test
    public void testError() throws Exception {
        // Arrange
        Double[] actuals = {1.0, 1.0, 1.0}, estimates = {1.0, 2.0, 3.0};
        LogarithmicStandardDeviationError lsd = new LogarithmicStandardDeviationError();

        // Act
        double error = lsd.error(actuals, estimates);

        // Assert
        assertEquals(0.7, error, .03);
    }
}