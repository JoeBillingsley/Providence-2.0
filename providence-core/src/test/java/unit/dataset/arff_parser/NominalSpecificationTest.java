package unit.dataset.arff_parser;

import dataset.arff_parser.NominalSpecification;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static test_helper.ThrowableCaptor.captureThrowable;

public class NominalSpecificationTest {

    @Test
    public void testParse() throws Exception {

        // Act
        NominalSpecification nominalSpecification = NominalSpecification.parse("{Low,Middle,High,'Extra high'}");

        double low = nominalSpecification.getValue("Low");
        double high = nominalSpecification.getValue("HIGH");
        double extraHigh = nominalSpecification.getValue("'Extra high'");

        Throwable doesNotExist = captureThrowable(() -> nominalSpecification.getValue("'Extra low'"));
        Throwable badStructure = captureThrowable(() -> NominalSpecification.parse("Low,Middle}"));

        // Assert
        assertEquals(0, low, 0);
        assertEquals(2, high, 0);
        assertEquals(3, extraHigh, 0);

        assertNotNull(doesNotExist);
        assertEquals(RuntimeException.class, doesNotExist.getClass());
        assertTrue(doesNotExist.getMessage()
                .contains("An unexpected value was encountered"));

        assertNotNull(badStructure);
        assertEquals(RuntimeException.class, badStructure.getClass());
        assertTrue(badStructure.getMessage()
                .contains("Nominal Specification types must have opening and closing curly brackets"));
    }
}