package unit.clustering;

import clustering.Cluster;
import dataset.Project;
import org.junit.Test;

import static org.junit.Assert.*;
import static test_helper.ThrowableCaptor.captureThrowable;

public class ClusterTest {

    @Test
    public void testGetMean() throws Exception {

        // Arrange
        Cluster normal = new Cluster();
        normal.addPoints(
                new Project(0, new Double[]{1.0, 1.0, 1.0}),
                new Project(1, new Double[]{2.0, 2.0, 2.0}),
                new Project(2, new Double[]{3.0, 3.0, 3.0})
        );

        Cluster empty = new Cluster();

        // Act
        Double[] mean = normal.getMean();
        Throwable err = captureThrowable(empty::getMean);

        // Assert
        assertArrayEquals(new Double[]{2.0, 2.0, 2.0}, mean);

        assertNotNull(err);
        assertEquals(IllegalArgumentException.class, err.getClass());
    }
}