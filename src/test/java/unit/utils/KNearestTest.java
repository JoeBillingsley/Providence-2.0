package unit.utils;

import evolve_nn.SEESolution;
import org.junit.Test;
import utils.KNearest;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static test_helper.SolutionHelper.makeSolution;
import static test_helper.SolutionHelper.makeSolutions;

public class KNearestTest {

    @Test
    public void testFind() {

        // Arrange
        int k = 3;

        Double[] one = { 5.0, 5.0, 5.0};
        Double[] two = { 4.0, 3.0, 4.0 };
        Double[] thr = { 2.0, 3.0, 1.0 };

        SEESolution solution = makeSolution(one);

        List<SEESolution> neighbouringSolutions = makeSolutions(
                new Double[]{ 0.0, 0.0, 0.0 },
                two,
                new Double[]{ 10.0, 10.0, 10.0 },
                one,
                new Double[]{ 12.0, 12.0, 12.0 },
                thr
        );

        // Act
        List<SEESolution> nearestSolutions = KNearest.find(solution, neighbouringSolutions, k);

        // Assert
        assertEquals(nearestSolutions.size(), k);

        assertArrayEquals(nearestSolutions.get(0).getObjectives(), one);
        assertArrayEquals(nearestSolutions.get(1).getObjectives(), two);
        assertArrayEquals(nearestSolutions.get(2).getObjectives(), thr);
    }
}