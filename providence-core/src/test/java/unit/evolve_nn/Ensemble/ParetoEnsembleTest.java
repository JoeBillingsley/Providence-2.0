package unit.evolve_nn.Ensemble;

import ensemble.ParetoEnsemble;
import evolve_nn.SEESolution;
import neural_network.FeedForwardPerceptron;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParetoEnsembleTest {

    @Test
    public void testGetResult() throws Exception {
        // Arrange
        SEESolution solutionOne = mock(SEESolution.class);
        when(solutionOne.getObjective(0)).thenReturn(0.0);
        when(solutionOne.getObjective(1)).thenReturn(1.0);
        when(solutionOne.getObjective(2)).thenReturn(1.0);

        FeedForwardPerceptron ffpOne = mock(FeedForwardPerceptron.class);
        when(ffpOne.executeNetwork(any())).thenReturn(new Double[]{0.0});

        when(solutionOne.getNeuralNetwork()).thenReturn(ffpOne);

        when(solutionOne.getNumberOfObjectives()).thenReturn(3);

        SEESolution solutionTwo = mock(SEESolution.class);
        when(solutionTwo.getObjective(0)).thenReturn(1.0);
        when(solutionTwo.getObjective(1)).thenReturn(0.0);
        when(solutionTwo.getObjective(2)).thenReturn(1.0);

        FeedForwardPerceptron ffpTwo = mock(FeedForwardPerceptron.class);
        when(ffpTwo.executeNetwork(any())).thenReturn(new Double[]{1.0});

        when(solutionTwo.getNeuralNetwork()).thenReturn(ffpTwo);

        SEESolution solutionThr = mock(SEESolution.class);
        when(solutionThr.getObjective(0)).thenReturn(1.0);
        when(solutionThr.getObjective(1)).thenReturn(1.0);
        when(solutionThr.getObjective(2)).thenReturn(0.0);

        FeedForwardPerceptron ffpThr = mock(FeedForwardPerceptron.class);
        when(ffpThr.executeNetwork(any())).thenReturn(new Double[]{2.0});

        when(solutionThr.getNeuralNetwork()).thenReturn(ffpThr);

        SEESolution solutionFor = mock(SEESolution.class);
        when(solutionFor.getObjective(0)).thenReturn(0.5);
        when(solutionFor.getObjective(1)).thenReturn(0.5);
        when(solutionFor.getObjective(2)).thenReturn(0.5);

        FeedForwardPerceptron ffpFor = mock(FeedForwardPerceptron.class);
        when(ffpFor.executeNetwork(any())).thenReturn(new Double[]{100.0});

        when(solutionFor.getNeuralNetwork()).thenReturn(ffpFor);

        List<SEESolution> candidates = new ArrayList<>();
        candidates.add(solutionOne);
        candidates.add(solutionTwo);
        candidates.add(solutionThr);
        candidates.add(solutionFor);

        ParetoEnsemble paretoEnsemble = new ParetoEnsemble(candidates);

        // Act
        Double result = paretoEnsemble.getResult(new Double[]{.0, .0, .0});

        // Assert
        assertEquals(1, result, 0);
    }


}