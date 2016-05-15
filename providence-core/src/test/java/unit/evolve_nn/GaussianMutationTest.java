package unit.evolve_nn;

import evolve_nn.GaussianMutation;
import evolve_nn.SEESolution;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class GaussianMutationTest {

    @Test
    public void testExecute() throws Exception {
        // Arrange
        SEESolution solutionOne = mock(SEESolution.class);
        when(solutionOne.getNumberOfVariables()).thenReturn(5);

        SEESolution solutionTwo = mock(SEESolution.class);
        when(solutionTwo.getNumberOfVariables()).thenReturn(0);

        GaussianMutation gaussianMutation = new GaussianMutation(1);

        // Act
        gaussianMutation.execute(solutionOne);

        // Assert
        verify(solutionOne, times(5)).setVariableValue(anyInt(), anyDouble());
        verify(solutionTwo, never()).setVariableValue(anyInt(), anyDouble());
    }


}