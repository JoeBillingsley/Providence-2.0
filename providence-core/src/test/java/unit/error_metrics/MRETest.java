package unit.error_metrics;

import error_metrics.MRE;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MRETest {

    @Test
    public void testCalculate() throws Exception {
        // Arrange
        // Act
        double perfect = MRE.calculate(3, 3);
        double small = MRE.calculate(10, 1);
        double large = MRE.calculate(100, 10);
        double inverse = MRE.calculate(1, 10);

        assertEquals(0, perfect, 0);
        assertEquals(.9, small, 0);
        assertEquals(.9, large, 0);
        assertEquals(9, inverse, 0);
    }
}