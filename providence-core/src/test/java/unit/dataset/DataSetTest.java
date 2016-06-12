package unit.dataset;

import dataset.DataSet;
import dataset.Feature;
import dataset.arff_parser.Attribute;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static test_helper.ThrowableCaptor.captureThrowable;

public class DataSetTest {

    @Test
    public void testAddFeature() throws Exception {
        // Arrange
        DataSet ds = new DataSet();

        List<Double> featureOne = new ArrayList<>();
        featureOne.add(1.0);
        featureOne.add(1.0);

        List<Double> featureTwo = new ArrayList<>();
        featureTwo.add(2.0);
        featureTwo.add(2.0);

        List<Double> featureThr = new ArrayList<>();
        featureThr.add(3.0);
        featureThr.add(3.0);

        List<Double> featureFour = new ArrayList<>();
        featureFour.add(4.0);
        featureFour.add(4.0);
        featureFour.add(4.0);

        Function<String, Double> blank = (x) -> 0.0;

        Attribute attr = mock(Attribute.class);

        // Act
        ds.addFeature(new Feature(attr, featureOne));
        ds.addFeature(new Feature(attr, featureTwo));
        ds.addFeature(new Feature(attr, featureThr));

        Throwable inconsistentNumberOfProjects =
                captureThrowable(() -> ds.addFeature(new Feature(new Attribute("Frs", blank), featureFour)));

        // Assert
        assertEquals(3, ds.getFeatures().size());
        assertEquals(featureTwo, ds.getFeatures().get(1).getAll());

        assertEquals(2, ds.getProjects().size());
        assertEquals(3.0, ds.getProject(0).getData()[2], 0);

        assertNotNull(inconsistentNumberOfProjects);
        assertEquals(IllegalArgumentException.class, inconsistentNumberOfProjects.getClass());
    }

    @Test
    public void testRemoveProject() throws Exception {
        // Arrange
        DataSet ds = new DataSet();

        List<Double> featureOne = new ArrayList<>();
        featureOne.add(1.0);
        featureOne.add(1.5);

        List<Double> featureTwo = new ArrayList<>();
        featureTwo.add(2.0);
        featureTwo.add(2.5);

        Attribute attr = mock(Attribute.class);

        // Act
        ds.addFeature(new Feature(attr, featureOne));
        ds.addFeature(new Feature(attr, featureTwo));

        ds.removeProject(0);

        // Assert
        assertEquals(2, ds.getFeatures().size());
        assertEquals(1, ds.getProjects().size());

        assertEquals(1.5, ds.getProject(0).getData()[0], 0);
    }
}