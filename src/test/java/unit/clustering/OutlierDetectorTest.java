package unit.clustering;

import clustering.OutlierDetector;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class OutlierDetectorTest {

    @Test
    public void testProcess() throws Exception {

        // Arrange
        OutlierDetector outlierRemover = new OutlierDetector();

        // region No outliers
        List<Double[]> noOutliersPoints = new ArrayList<>();
        // Cluster one
        noOutliersPoints.add(new Double[]{0.0, 0.0});
        noOutliersPoints.add(new Double[]{0.0, 1.0});
        noOutliersPoints.add(new Double[]{1.0, 0.0});
        noOutliersPoints.add(new Double[]{1.0, 1.0});

        // Cluster two
        noOutliersPoints.add(new Double[]{4.0, 4.0});
        noOutliersPoints.add(new Double[]{4.0, 5.0});
        noOutliersPoints.add(new Double[]{5.0, 4.0});
        noOutliersPoints.add(new Double[]{5.0, 5.0});

        // Cluster three
        noOutliersPoints.add(new Double[]{9.0, 2.0});
        noOutliersPoints.add(new Double[]{10.0, 2.5});
        noOutliersPoints.add(new Double[]{11.0, 3.0});
        // endregion

        // region Some outliers
        List<Double[]> someOutliersPoints = new ArrayList<>();
        // Core
        someOutliersPoints.add(new Double[]{1.0, 1.0});
        someOutliersPoints.add(new Double[]{1.0, 2.0});
        someOutliersPoints.add(new Double[]{2.0, 1.0});
        someOutliersPoints.add(new Double[]{2.0, 2.0});

        // Outliers
        someOutliersPoints.add(new Double[]{2.0, 50.0});
        someOutliersPoints.add(new Double[]{50.0, 2.0});
        // endregion

        // Act
        List<Double[]> noOutliers = outlierRemover.getOutliers(noOutliersPoints);
        List<Double[]> someOutliers = outlierRemover.getOutliers(someOutliersPoints);

        // Assert
        assertTrue(noOutliers.size() == 0);
        assertTrue(someOutliers.size() == 2);
    }
}