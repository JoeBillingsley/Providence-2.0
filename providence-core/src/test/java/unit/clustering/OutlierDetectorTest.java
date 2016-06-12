package unit.clustering;

import clustering.Cluster;
import clustering.IClustering;
import clustering.OutlierDetector;
import dataset.Project;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OutlierDetectorTest {

    @Test
    public void testProcess() throws Exception {

        // Arrange
        IClustering clustering = mock(IClustering.class);

        OutlierDetector outlierRemover = new OutlierDetector(clustering);

        List<Project> projects = Arrays.asList(
                // Cluster One - Normal
                new Project(0, new Double[]{0.0, 0.0}),
                new Project(1, new Double[]{1.0, 0.0}),
                new Project(2, new Double[]{0.0, 1.0}),
                new Project(3, new Double[]{1.0, 1.0}),

                // Cluster Two -  Too few points
                new Project(4, new Double[]{5.0, 0.0}),
                new Project(5, new Double[]{5.0, 1.0}),

                // Cluster Three - Dissimilar point to cluster
                new Project(6, new Double[]{10.0, 10.0}),
                new Project(7, new Double[]{10.0, 11.0}),
                new Project(8, new Double[]{11.0, 10.0}),
                new Project(9, new Double[]{11.0, 11.0}),
                new Project(10, new Double[]{45.0, 50.0})
        );

        Cluster clusterOne = new Cluster();
        clusterOne.addPoints(projects.get(0), projects.get(1), projects.get(2), projects.get(3));

        Cluster clusterTwo = new Cluster();
        clusterTwo.addPoints(projects.get(4), projects.get(5));

        Cluster clusterThree = new Cluster();
        clusterThree.addPoints(projects.get(6), projects.get(7), projects.get(8), projects.get(9), projects.get(10));

        when(clustering.run(any(), anyInt())).thenReturn(
                new Cluster[]{
                        clusterOne,
                        clusterTwo,
                        clusterThree
                }
        );

        // Act
        List<Project> outliers = outlierRemover.getOutliers(projects);

        // Assert
        assertTrue(outliers.size() == 3);
        assertTrue(outliers.contains(projects.get(4)));
        assertTrue(outliers.contains(projects.get(5)));
        assertTrue(outliers.contains(projects.get(10)));
    }
}