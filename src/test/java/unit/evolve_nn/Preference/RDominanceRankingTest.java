package unit.evolve_nn.Preference;

import evolve_nn.RDominanceRanking;
import evolve_nn.SEESolution;
import org.junit.Before;
import org.junit.Test;
import org.uma.jmetal.util.solutionattribute.Ranking;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RDominanceRankingTest {

    private List<SEESolution> testSolutions;

    private Double[] aspirationPoint = {1.0, 1.0, 1.0};
    private double nonRDominanceThreshold = .2;

    @Before
    public void init() {
        // region Set up test solution
        testSolutions = new ArrayList<>();

        SEESolution a = new SEESolution(3, null);
        a.setObjective(0, 1);
        a.setObjective(1, 1);
        a.setObjective(2, 2);
        testSolutions.add(a);

        SEESolution b = new SEESolution(3, null);
        b.setObjective(0, 1);
        b.setObjective(1, 2);
        b.setObjective(2, 1);
        testSolutions.add(b);

        SEESolution c = new SEESolution(3, null);
        c.setObjective(0, 2);
        c.setObjective(1, 2);
        c.setObjective(2, 2);
        testSolutions.add(c);

        SEESolution d = new SEESolution(3, null);
        d.setObjective(0, 3);
        d.setObjective(1, 3);
        d.setObjective(2, 3);
        testSolutions.add(d);
        // endregion
    }

    @Test
    public void testComputeRanking() {
        // Arrange
        RDominanceRanking selection = new RDominanceRanking(aspirationPoint, nonRDominanceThreshold);

        // Act
        Ranking<SEESolution> ranking = selection.computeRanking(testSolutions);
        List<SEESolution> optimalFront = ranking.getSubfront(0);

        SEESolution expectedOne = testSolutions.get(0);
        SEESolution expectedTwo = testSolutions.get(1);

        // Assert
        assertEquals(2, optimalFront.size());

        SEESolution optimalOne = optimalFront.get(0);
        SEESolution optimalTwo = optimalFront.get(1);

        assertTrue(expectedOne == optimalOne || expectedOne == optimalTwo);
        assertTrue(expectedTwo == optimalOne || expectedTwo == optimalTwo);
    }

    @Test
    public void testGetSubfront() {
        // Arrange
        RDominanceRanking selection = new RDominanceRanking(aspirationPoint, nonRDominanceThreshold);

        // Act
        Ranking<SEESolution> ranking = selection.computeRanking(testSolutions);
        List<SEESolution> optimalFront = ranking.getSubfront(0);
        List<SEESolution> secondFront = ranking.getSubfront(1);
        List<SEESolution> lastFront = ranking.getSubfront(2);

        // Assert
        assertEquals(2, optimalFront.size());
        assertEquals(1, secondFront.size());
        assertEquals(1, lastFront.size());
    }

    @Test
    public void testGetNumberOfSubfronts() throws Exception {
        // Arrange
        RDominanceRanking selection = new RDominanceRanking(aspirationPoint, nonRDominanceThreshold);

        // Act
        Ranking<SEESolution> ranking = selection.computeRanking(testSolutions);

        // Assert
        assertEquals(3, ranking.getNumberOfSubfronts());
    }
}