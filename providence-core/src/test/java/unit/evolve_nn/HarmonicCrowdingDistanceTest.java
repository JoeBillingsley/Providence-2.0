package unit.evolve_nn;

import evolve_nn.HarmonicCrowdingDistance;
import evolve_nn.SEESolution;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static test_helper.SolutionHelper.makeSolutions;

public class HarmonicCrowdingDistanceTest {

    @Test
    public void testComputeDensityEstimator() {
        // Arrange
        List<SEESolution> clumped = makeSolutions(
                new Double[] { 3.0, 1.0, 3.0 },
                new Double[] { 4.0, 2.0, 2.0 },
                new Double[] { 5.0, 3.0, 2.0 },
                new Double[] { 6.0, 5.0, 7.0 });

        List<SEESolution> scattered = makeSolutions(
                new Double[] { 3.0, 4.0, 5.0 },
                new Double[] { 10.0, 10.0, 10.0 },
                new Double[] { 18.0, 22.0, 14.0 },
                new Double[] { 30.0, 25.0, 15.0 }
        );

        HarmonicCrowdingDistance crowdingDistance = new HarmonicCrowdingDistance(2);

        // Act
        crowdingDistance.setSearchPopulation(clumped);
        crowdingDistance.computeDensityEstimator(clumped);

        crowdingDistance.setSearchPopulation(scattered);
        crowdingDistance.computeDensityEstimator(scattered);

        // Assert
        Object attrId = crowdingDistance.getAttributeID();

        Double clOne = (Double) clumped.get(0).getAttribute(attrId);
        Double clTwo = (Double) clumped.get(2).getAttribute(attrId);
        assertTrue(clOne > clTwo);

        Double scOne = (Double) scattered.get(1).getAttribute(attrId);
        Double scTwo = (Double) scattered.get(3).getAttribute(attrId);
        assertTrue(scOne < scTwo);

        assertTrue("The mean average harmonic distance of close points is lower than that of points that are far apart",
                ((clOne + clTwo) / 2) < ((scOne + scTwo) / 2)
        );
    }


}