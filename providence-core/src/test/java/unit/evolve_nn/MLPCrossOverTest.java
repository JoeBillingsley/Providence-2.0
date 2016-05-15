package unit.evolve_nn;

import evolve_nn.MLPCrossOver;
import evolve_nn.SEESolution;
import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class MLPCrossOverTest {

    @Test
    public void testExecute() throws Exception {
        // Arrange
        SEESolution solutionOne = mock(SEESolution.class);
        SEESolution solutionTwo = mock(SEESolution.class);
        SEESolution solutionThree = mock(SEESolution.class);

        when(solutionOne.copy()).thenReturn(mock(SEESolution.class));
        when(solutionOne.getNumberOfVariables()).thenReturn(5);

        ArrayList<SEESolution> population = new ArrayList<>();
        population.add(solutionOne);
        population.add(solutionTwo);
        population.add(solutionThree);

        MLPCrossOver mlp = new MLPCrossOver(1, 100);

        // Act
        mlp.execute(population);

        // Assert
        verify(solutionOne, times(5)).getVariableValue(anyInt());
        verify(solutionTwo, times(5)).getVariableValue(anyInt());
        verify(solutionThree, times(5)).getVariableValue(anyInt());
    }
}