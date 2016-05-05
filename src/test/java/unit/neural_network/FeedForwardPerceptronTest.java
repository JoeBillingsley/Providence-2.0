package unit.neural_network;

import neural_network.FeedForwardPerceptron;
import neural_network.IStepFunction;
import neural_network.IWeightInitialiser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static test_helper.ThrowableCaptor.captureThrowable;

public class FeedForwardPerceptronTest {

    @Test
    public void testExecuteNetwork() {
        // Arrange
        // Initialise all weights to 1.0;
        IWeightInitialiser simpleConsistentWeights = mock(IWeightInitialiser.class);

        doAnswer(
                invocation -> {
                    int length = (int) invocation.getArguments()[0];
                    Double[] weights = new Double[length];

                    Arrays.fill(weights, 1.0);

                    return weights;
                }
        ).when(simpleConsistentWeights).init(anyInt(), anyDouble(), anyDouble());

        // Return the provided weight
        IStepFunction reflectWeightStepFunction = mock(IStepFunction.class);
        doAnswer(
                invocation -> invocation.getArguments()[0]
        ).when(reflectWeightStepFunction).response(anyDouble());

        IStepFunction outputWeightStepFunction = mock(IStepFunction.class);
        doAnswer(
                invocation -> invocation.getArguments()[0]
        ).when(outputWeightStepFunction).response(anyDouble());

        FeedForwardPerceptron nn = new FeedForwardPerceptron(1, 3, 2, 2, 0.5,
                simpleConsistentWeights,
                reflectWeightStepFunction, outputWeightStepFunction);

        // Act
        Double[] values = nn.executeNetwork(new Double[]{1.0});

        // Broken
        Throwable wrongNumOfInputs = captureThrowable(
                () -> nn.executeNetwork(new Double[]{1.0, 2.0}));

        // Assert
        verify(simpleConsistentWeights, times(1)).init(anyInt(), anyDouble(), anyDouble());
        verify(reflectWeightStepFunction, times(6)).response(anyDouble());
        verify(outputWeightStepFunction, times(2)).response(anyDouble());

        assertArrayEquals(new Double[]{2.5, 2.5}, values);

        // Broken
        assertNotNull(wrongNumOfInputs);
        assertEquals(IllegalArgumentException.class, wrongNumOfInputs.getClass());
    }

    @Test
    public void testGetWeightsForNode() {
        // Arrange
        // Simple
        IWeightInitialiser simpleConsistentWeights = mock(IWeightInitialiser.class);
        when(simpleConsistentWeights.init(anyInt(), anyDouble(), anyDouble())).thenReturn(
                new Double[]{
                        1.0,
                        2.0
                });

        FeedForwardPerceptron simpleNN = new FeedForwardPerceptron(1, 1, 1, 1, 0.0, simpleConsistentWeights);

        // Complex
        IWeightInitialiser complexConsistentWeights = mock(IWeightInitialiser.class);
        when(complexConsistentWeights.init(anyInt(), anyDouble(), anyDouble())).thenReturn(
                new Double[]{
                        1.0, 1.0, 1.0,
                        2.0, 2.1, 2.2, 2.0, 2.1, 2.2, 2.0, 2.1, 2.2,
                        3.0, 3.0, 3.0
                });

        FeedForwardPerceptron complexNN = new FeedForwardPerceptron(1, 3, 1, 2, 0.0, complexConsistentWeights);

        // Act
        // Simple
        Double[] simpleInputToHidden = simpleNN.getWeightsToNode(1, 0);
        Double[] simpleHiddenToOutput = simpleNN.getWeightsToNode(2, 0);

        // Complex
        Double[] complexInputToHidden = complexNN.getWeightsToNode(1, 0);
        Double[] complexHiddenToHidden = complexNN.getWeightsToNode(2, 2);
        Double[] complexHiddenToOutput = complexNN.getWeightsToNode(3, 0);

        // Broken
        Throwable inputLayer = captureThrowable(() -> simpleNN.getWeightsToNode(0, 0));

        // Assert
        // Simple
        assertArrayEquals(new Double[]{1.0}, simpleInputToHidden);
        assertArrayEquals(new Double[]{2.0}, simpleHiddenToOutput);

        // Complex
        assertArrayEquals(new Double[]{1.0}, complexInputToHidden);
        assertArrayEquals(new Double[]{2.0, 2.1, 2.2}, complexHiddenToHidden);
        assertArrayEquals(new Double[]{3.0, 3.0, 3.0}, complexHiddenToOutput);

        // Broken
        Assert.assertNotNull(inputLayer);
        Assert.assertEquals(IndexOutOfBoundsException.class, inputLayer.getClass());
    }

    @Test
    public void testCountNodesInLayer() {
        // ARRANGE
        FeedForwardPerceptron nn = new FeedForwardPerceptron(1, 3, 5, 7, 0.0);

        // ACT
        int inputLayerCount = nn.countNodesInLayer(0);
        int hiddenLayerCount = nn.countNodesInLayer(1);
        int secondHiddenLayerCount = nn.countNodesInLayer(2);
        int outputLayerCount = nn.countNodesInLayer(
                nn.getOutputLayerIndex()
        );

        Throwable negativeLayer = captureThrowable(() -> nn.countNodesInLayer(-1));
        Throwable tooHighLayer = captureThrowable(() -> nn.countNodesInLayer(100));

        // ASSERT
        assertEquals(inputLayerCount, 1);
        assertEquals(hiddenLayerCount, 3);
        assertEquals(secondHiddenLayerCount, 3);
        assertEquals(outputLayerCount, 5);

        assertNotNull(negativeLayer);
        assertEquals(IndexOutOfBoundsException.class, negativeLayer.getClass());

        assertNotNull(tooHighLayer);
        assertEquals(IndexOutOfBoundsException.class, tooHighLayer.getClass());
    }

    @Test
    public void testCountWeightsInLayer() {
        // Arrange
        FeedForwardPerceptron nn = new FeedForwardPerceptron(1, 3, 5, 7, 0.0);

        // Act
        int layerZeroCount = nn.countWeightsInLayer(0);
        int layerOneCount = nn.countWeightsInLayer(1);
        int lastLayerCount = nn.countWeightsInLayer(nn.getOutputLayerIndex() - 1);

        Throwable negativeLayer = captureThrowable(() -> nn.countWeightsInLayer(-3));
        Throwable tooHighLayer = captureThrowable(() -> nn.countWeightsInLayer(25));

        // Assert
        assertEquals(3, layerZeroCount);
        assertEquals(9, layerOneCount);
        assertEquals(15, lastLayerCount);

        assertNotNull(negativeLayer);
        assertEquals(IndexOutOfBoundsException.class, negativeLayer.getClass());

        assertNotNull(tooHighLayer);
        assertEquals(IndexOutOfBoundsException.class, tooHighLayer.getClass());
    }

    @Test
    public void testGetOutputLayerIndex() {
        // Arrange
        FeedForwardPerceptron nn = new FeedForwardPerceptron(1, 1, 1, 1, 0.0);

        // Act
        int i = nn.getOutputLayerIndex();

        // Assert
        assertEquals(2, i);
    }
}