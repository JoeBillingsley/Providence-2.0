package unit.dataset;

import dataset.DataSet;
import dataset.Feature;
import dataset.InputOutput;
import dataset.TrainingData;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static test_helper.ThrowableCaptor.captureThrowable;

public class TrainingDataTest {

    @Test
    public void testSetDependantFeatures() {
        // Arrange

        Feature aFeature = mock(Feature.class);
        when(aFeature.getName()).thenReturn("A");

        Feature bFeature = mock(Feature.class);
        when(bFeature.getName()).thenReturn("B");

        Feature cFeature = mock(Feature.class);
        when(cFeature.getName()).thenReturn("C");

        List<Feature> features = new ArrayList<>();
        features.add(aFeature);
        features.add(bFeature);
        features.add(cFeature);

        List<Double[]> projects = new ArrayList<>();
        projects.add(new Double[]{0.0, 1.0, 2.0});
        projects.add(new Double[]{0.0, 1.0, 2.0});
        projects.add(new Double[]{0.0, 1.0, 2.0});

        DataSet ds = mock(DataSet.class);
        when(ds.getFeatures()).thenReturn(features);
        when(ds.getProjects()).thenReturn(projects);

        // Act
        ArrayList<Feature> outputFeatures = new ArrayList<>();
        outputFeatures.add(cFeature);

        TrainingData trainingData = new TrainingData(ds, outputFeatures);

        InputOutput[] data = trainingData.getAll();

        // Assert
        assertEquals(3, data.length);
        assertEquals(2, data[0].getInputs().length);
        assertEquals(1, data[0].getOutputs().length);

        assertEquals(1, data[0].getInputs()[1], 0);
        assertEquals(2, data[0].getOutputs()[0], 0);
    }

    @Test
    public void testGetSplitOfRatio() throws Exception {
        // Arrange
        Feature aFeature = mock(Feature.class);
        when(aFeature.getName()).thenReturn("A");

        Feature bFeature = mock(Feature.class);
        when(bFeature.getName()).thenReturn("B");

        Feature cFeature = mock(Feature.class);
        when(cFeature.getName()).thenReturn("C");

        List<Feature> features = new ArrayList<>();
        features.add(aFeature);
        features.add(bFeature);
        features.add(cFeature);

        List<Double[]> projects = new ArrayList<>();
        projects.add(new Double[]{0.0, 1.0, 2.0});
        projects.add(new Double[]{0.0, 1.0, 2.0});
        projects.add(new Double[]{0.0, 1.0, 2.0});

        DataSet ds = mock(DataSet.class);
        when(ds.getFeatures()).thenReturn(features);
        when(ds.getProjects()).thenReturn(projects);

        TrainingData trainingData = new TrainingData(ds, features);

        // Act
        InputOutput[][] split = trainingData.getSplitOfRatio(.7);

        Throwable tooLow = captureThrowable(() -> trainingData.getSplitOfRatio(-.1));
        Throwable tooHigh = captureThrowable(() -> trainingData.getSplitOfRatio(1.1));

        // Assert
        assertEquals(2, split[0].length);
        assertEquals(1, split[1].length);

        assertNotEquals(split[0][0], split[1][0]);
        assertNotEquals(split[0][1], split[1][0]);

        assertNotNull(tooLow);
        assertEquals(IllegalArgumentException.class, tooLow.getClass());

        assertNotNull(tooHigh);
        assertEquals(IllegalArgumentException.class, tooHigh.getClass());
    }
}