package unit.search_parameters;

import error_metrics.ErrorMetric;
import org.junit.Test;
import search_parameters.SearchParameters;
import search_parameters.SearchParametersFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

public class SearchParametersTest {

    @Test
    public void testCopy() throws Exception {
        // Arrange
        SearchParameters original = SearchParametersFactory.getDefault();

        // Act
        SearchParameters copy = original.copy();
        copy.getErrorMetrics().add(mock(ErrorMetric.class));

        // Assert
        assertEquals(original.getPopulationSize(), copy.getPopulationSize());
        assertEquals(original.outputColumnsProperty().size(), copy.outputColumnsProperty().size());

        assertNotEquals(original.getErrorMetrics(), copy.getErrorMetrics());
        assertEquals(1, copy.getErrorMetrics().size());
    }
}