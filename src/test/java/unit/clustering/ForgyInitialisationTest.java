package unit.clustering;

import clustering.ForgyInitialisation;
import clustering.IMeanInitialiser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static test_helper.ThrowableCaptor.captureThrowable;

public class ForgyInitialisationTest {

    @Test
    public void testInitialiseMeans() throws Exception {

        // Arrange
        IMeanInitialiser init = new ForgyInitialisation();

        List<Double[]> points = new ArrayList<>();
        points.add(new Double[]{0.0, 0.0});
        points.add(new Double[]{1.0, 1.0});
        points.add(new Double[]{2.0, 2.0});
        points.add(new Double[]{3.0, 3.0});
        points.add(new Double[]{4.0, 4.0});
        points.add(new Double[]{5.0, 5.0});

        // Act
        List<Double[]> means = init.initialiseMeans(3, points);
        Throwable tooManyMeans = captureThrowable(() -> init.initialiseMeans(100, points));

        // Assert
        assertEquals(3, means.size());

        assertNotNull(tooManyMeans);
        assertEquals(IndexOutOfBoundsException.class, tooManyMeans.getClass());

        for (int i = 0; i < means.size(); i++) {
            assertTrue(points.contains(means.get(i)));

            for (int j = 0; j < means.size(); j++) {
                assertFalse(i != j && Arrays.equals(means.get(i), means.get(j)));
            }
        }

    }
}