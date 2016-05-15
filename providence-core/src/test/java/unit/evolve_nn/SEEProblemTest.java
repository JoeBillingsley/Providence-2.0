package unit.evolve_nn;

import dataset.InputOutput;
import error_metrics.ErrorMetric;
import evolve_nn.SEEProblem;
import evolve_nn.SEESolution;
import neural_network.INeuralNetwork;
import org.junit.Test;
import test_helper.SolutionHelper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class SEEProblemTest {

    @Test
    public void testEvaluate() throws Exception {
        // Arrange
        InputOutput[] trainingData = new InputOutput[1];

        InputOutput inputOutput = mock(InputOutput.class);
        when(inputOutput.getOutputs()).thenReturn(new Double[]{1.0});

        trainingData[0] = inputOutput;

        List<ErrorMetric> maximisingEMs = new ArrayList<>();
        ErrorMetric maximisingEM = mock(ErrorMetric.class);
        when(maximisingEM.isMinimising()).thenReturn(false);
        when(maximisingEM.error(any(), any())).thenReturn(1.0);
        maximisingEMs.add(maximisingEM);

        List<ErrorMetric> minimisingEMs = new ArrayList<>();
        ErrorMetric minimisingEM = mock(ErrorMetric.class);
        when(minimisingEM.isMinimising()).thenReturn(true);
        when(minimisingEM.error(any(), any())).thenReturn(4.0);
        minimisingEMs.add(minimisingEM);

        INeuralNetwork nn = mock(INeuralNetwork.class);
        when(nn.executeNetwork(any())).thenReturn(new Double[]{2.0});

        SEESolution solution = spy(SolutionHelper.makeSolution(new Double[]{1.0}));
        when(solution.getNeuralNetwork()).thenReturn(nn);

        SEEProblem maximisingProb = new SEEProblem(trainingData, maximisingEMs);
        SEEProblem minimisingProb = new SEEProblem(trainingData, minimisingEMs);

        // Act
        maximisingProb.evaluate(solution);
        double maximising = solution.getObjective(0);

        minimisingProb.evaluate(solution);
        double minimising = solution.getObjective(0);

        // Assert
        assertEquals(0, maximising, 0);
        assertEquals(4, minimising, 0);
    }
}