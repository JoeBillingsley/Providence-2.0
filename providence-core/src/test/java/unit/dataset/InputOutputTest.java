package unit.dataset;

import dataset.InputOutput;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static test_helper.ThrowableCaptor.captureThrowable;

public class InputOutputTest {

    @Test
    public void testConstructInputOutput() throws Exception {
        // Arrange
        Double[] project = {0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0};

        // Act
        InputOutput normal = new InputOutput(project, new int[]{0, 3, 4});
        Throwable noOutputs = captureThrowable(() -> new InputOutput(project, new int[]{}));

        Double[] inputs = normal.getInputs();
        Double[] outputs = normal.getOutputs();

        // Assert
        assertEquals(4, inputs.length);
        assertEquals(3, outputs.length);

        assertEquals(2.0, inputs[1], 0);
        assertEquals(4.0, outputs[2], 0);

        assertNotNull(noOutputs);
        assertEquals(IllegalArgumentException.class, noOutputs.getClass());
    }

}