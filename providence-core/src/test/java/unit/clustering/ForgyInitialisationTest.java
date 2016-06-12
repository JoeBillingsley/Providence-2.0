package unit.clustering;

import clustering.ForgyInitialisation;
import clustering.IMeanInitialiser;
import dataset.Project;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static test_helper.ThrowableCaptor.captureThrowable;

public class ForgyInitialisationTest {

    @Test
    public void testInitialiseMeans() throws Exception {

        // Arrange
        IMeanInitialiser init = new ForgyInitialisation();

        List<Project> points = Arrays.asList(
                new Project(0, new Double[]{0.0, 0.0}),
                new Project(1, new Double[]{1.0, 1.0}),
                new Project(2, new Double[]{2.0, 2.0}),
                new Project(3, new Double[]{3.0, 3.0}),
                new Project(4, new Double[]{4.0, 4.0}),
                new Project(5, new Double[]{5.0, 5.0})
        );

        // Act
        List<Double[]> means = init.initialiseMeans(3, points);
        Throwable tooManyMeans = captureThrowable(() -> init.initialiseMeans(100, points));

        // Assert
        assertEquals(3, means.size());

        assertNotNull(tooManyMeans);
        assertEquals(IndexOutOfBoundsException.class, tooManyMeans.getClass());

        for (int i = 0; i < means.size(); i++) {

            assertTrue(meanExistsInPopulation(means.get(i), points));

            for (int j = 0; j < means.size(); j++) {
                assertFalse(i != j && Arrays.equals(means.get(i), means.get(j)));
            }
        }
    }

    private boolean meanExistsInPopulation(Double[] mean, List<Project> projects) {
        for (Project project : projects) {
            if (Arrays.equals(project.getData(), mean))
                return true;
        }

        return false;
    }
}