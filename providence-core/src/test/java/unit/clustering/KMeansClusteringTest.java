package unit.clustering;

import clustering.Cluster;
import clustering.IMeanInitialiser;
import clustering.KMeansClustering;
import dataset.Project;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KMeansClusteringTest {

    @Test
    public void testRun() {

        // Arrange
        List<Project> points = Arrays.asList(
                // Cluster one
                new Project(0, new Double[]{0.0, 0.0}),
                new Project(1, new Double[]{1.0, 0.0}),
                new Project(2, new Double[]{0.0, 1.0}),
                new Project(3, new Double[]{1.0, 1.0}),

                // Cluster two
                new Project(4, new Double[]{7.0, 2.0}),
                new Project(5, new Double[]{8.0, 4.0}),
                new Project(6, new Double[]{9.0, 2.0}),

                // Cluster three
                new Project(7, new Double[]{7.0, 7.0})
        );

        IMeanInitialiser init = mock(IMeanInitialiser.class);

        List<Double[]> initVal = new ArrayList<>();
        initVal.add(new Double[]{0.0, 0.0});
        initVal.add(new Double[]{7.0, 0.0});
        initVal.add(new Double[]{7.0, 9.0});

        when(init.initialiseMeans(anyInt(), any())).thenReturn(initVal);

        KMeansClustering kMeans = new KMeansClustering(init);

        // Act
        Cluster[] clusters = kMeans.run(points, 3);

        // Assert
        assertEquals(3, clusters.length);
        assertEquals(8, clusters[0].getPoints().size() + clusters[1].getPoints().size() + clusters[2].getPoints().size());

        for (int i = 0; i < 3; i++) {
            assertTrue(clusters[i].getPoints().size() == 4
                    || clusters[i].getPoints().size() == 3
                    || clusters[i].getPoints().size() == 1);
        }
    }
}