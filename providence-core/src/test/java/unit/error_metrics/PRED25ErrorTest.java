package unit.error_metrics;

import error_metrics.PRED25Error;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static test_helper.ThrowableCaptor.captureThrowable;

public class PRED25ErrorTest {

    @Test
    public void testError() throws Exception {
        // Arrange
        PRED25Error error = new PRED25Error();

        // Act
        double normal = error.error(new Double[]{1.0, 1.0, 1.0, 1.0}, new Double[]{1.0, .75, .50, .25});
        Throwable wrongArgs = captureThrowable(() -> error.error(new Double[]{1.0, .5}, new Double[]{1.0}));
        Throwable noArgs = captureThrowable(() -> error.error(new Double[]{1.0, .5}, new Double[]{}));

        // Assert
        assertEquals(.5, normal, 0);

        assertNotNull(wrongArgs);
        assertEquals(IllegalArgumentException.class, wrongArgs.getClass());

        assertNotNull(noArgs);
        assertEquals(IllegalArgumentException.class, noArgs.getClass());
    }
}