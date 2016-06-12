package unit.clustering;

import clustering.Cluster;
import clustering.Silhouette;
import dataset.Project;
import org.junit.Test;

import static org.junit.Assert.*;
import static test_helper.ThrowableCaptor.captureThrowable;

public class SilhouetteTest {

    @Test
    public void testGetSilhouetteValue() {
        // Arrange
        // Point in the middle of a cross
        Cluster clusterOne = new Cluster();
        clusterOne.addPoints(
                new Project(0, new Double[]{0.0, 1.0}),
                new Project(1, new Double[]{1.0, 0.0}),
                new Project(2, new Double[]{2.0, 1.0}),
                new Project(3, new Double[]{1.0, 2.0})
        );

        // Vertical line 1
        Cluster clusterTwo = new Cluster();
        clusterTwo.addPoints(
                new Project(4, new Double[]{0.0, 1.0}),
                new Project(5, new Double[]{0.0, 2.0}),
                new Project(6, new Double[]{0.0, 3.0})
        );

        // Vertical line 2
        Cluster clusterThr = new Cluster();
        clusterThr.addPoints(
                new Project(7, new Double[]{50.0, 1.0}),
                new Project(8, new Double[]{50.0, 2.0}),
                new Project(9, new Double[]{50.0, 3.0})
        );

        // Act
        double good = Silhouette.getSilhouetteValue(
                new Project(20, new Double[]{1.0, 1.0}), clusterOne, clusterThr);

        double equal = Silhouette.getSilhouetteValue(
                new Project(21, new Double[]{25.0, 1.0}), clusterTwo, clusterThr);

        double poor = Silhouette.getSilhouetteValue(
                new Project(22, new Double[]{4.0, 1.0}), clusterThr, clusterTwo);

        Throwable tooFewClusters = captureThrowable(
                () -> Silhouette.getSilhouetteValue(clusterOne.getPoints().get(0), clusterOne));

        // Assert
        assertTrue(good <= 1 && good >= -1);
        assertTrue(equal <= 1 && equal >= -1);
        assertTrue(poor <= 1 && poor >= -1);

        assertEquals(good, 1, .1);
        assertEquals(equal, 0, .1);
        assertEquals(poor, -1, .1);

        assertNotNull(tooFewClusters);
        assertEquals(IllegalArgumentException.class, tooFewClusters.getClass());
    }
}