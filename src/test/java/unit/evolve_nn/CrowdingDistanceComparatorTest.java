package unit.evolve_nn;

import evolve_nn.CrowdingDistanceComparator;
import org.junit.Test;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.solutionattribute.DensityEstimator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CrowdingDistanceComparatorTest {

    @Test
    public void testCompare() throws Exception {
        // Arrange
        Solution solutionOne = mock(Solution.class);
        Solution solutionTwo = mock(Solution.class);
        Solution solutionThr = mock(Solution.class);

        DensityEstimator densityEstimator = mock(DensityEstimator.class);
        when(densityEstimator.getAttribute(solutionOne)).thenReturn(1.0);
        when(densityEstimator.getAttribute(solutionTwo)).thenReturn(2.0);
        when(densityEstimator.getAttribute(solutionThr)).thenReturn(2.0);

        CrowdingDistanceComparator cdc = new CrowdingDistanceComparator<>(densityEstimator);

        // Act
        int xLessY = cdc.compare(solutionOne, solutionTwo);
        int xMoreY = cdc.compare(solutionTwo, solutionOne);
        int xEqY = cdc.compare(solutionTwo, solutionThr);

        // Assert
        assertEquals(xLessY, 1);
        assertEquals(xMoreY, -1);
        assertEquals(xEqY, 0);

    }
}