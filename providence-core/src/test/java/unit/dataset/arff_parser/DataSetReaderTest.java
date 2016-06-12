package unit.dataset.arff_parser;

import dataset.DataSet;
import dataset.Feature;
import dataset.arff_parser.DataSetReader;
import org.junit.Test;
import test_helper.ThrowableCaptor;

import java.io.BufferedReader;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataSetReaderTest {

    @Test
    public void testOpen() throws Exception {
        // Arrange
        // region Ordinary
        BufferedReader commonSrc = mock(BufferedReader.class);
        when(commonSrc.readLine())
                .thenReturn("% A comment that should be ignored")
                .thenReturn("@relation not_relevant")
                .thenReturn("")
                .thenReturn("@attribute ProjectID numeric")
                .thenReturn("@attribute PREC {'Very High','Extra High',Nominal,'Very Low',High,Low}")
                .thenReturn("@attribute STOR {Low, 'Very Low',High,'Extra High','Very Very High',Nominal}")
                .thenReturn("")
                .thenReturn("@data")
                .thenReturn("1,High,Nominal")
                .thenReturn("2,'Very High','Very Very High'")
                .thenReturn(null);
        // endregion

        // region Missing data
        BufferedReader missingDataSrc = mock(BufferedReader.class);
        when(missingDataSrc.readLine())
                .thenReturn("@attribute ProjectID numeric")
                .thenReturn("@attribute PREC {'Very High','Extra High',Nominal,'Very Low',High,Low}")
                .thenReturn("@attribute STOR {Low,'Very Low',High,'Extra High','Very High',Nominal}")
                .thenReturn(null);
        // endregion

        // region Missing attribute
        BufferedReader missingAttrSrc = mock(BufferedReader.class);
        when(missingAttrSrc.readLine())
                .thenReturn("@data")
                .thenReturn("1,'Very Very High',Nominal")
                .thenReturn("2,'Extra High',High")
                .thenReturn(null);
        // endregion

        // region Mismatching attributes
        BufferedReader mismatchingAttributeSrc = mock(BufferedReader.class);
        when(mismatchingAttributeSrc.readLine())
                .thenReturn("@attribute ProjectID numeric")
                .thenReturn("@attribute PREC {A,B,C,D,E}")
                .thenReturn("@data")
                .thenReturn("1,'Very High'")
                .thenReturn(null);
        // endregion

        // region Misaligned Attributes
        BufferedReader misalignedAttributesSrc = mock(BufferedReader.class);
        when(misalignedAttributesSrc.readLine())
                .thenReturn("@attribute ProjectID numeric")
                .thenReturn("@attribute PREC {'Very High','Extra High',Nominal,'Very Low',High,Low}")
                .thenReturn("@data")
                .thenReturn("1,'Very High',Nominal")
                .thenReturn("2,'Extra High',High")
                .thenReturn(null);
        // endregion

        // region Unknown type
        BufferedReader unknownTypeSrc = mock(BufferedReader.class);
        when(unknownTypeSrc.readLine())
                .thenReturn("@attribute PREC amadeuptype")
                .thenReturn("@data")
                .thenReturn("'Very High'")
                .thenReturn(null);
        //endregion

        // Act
        DataSet ordinary = DataSetReader.open(commonSrc);
        DataSet missingData = DataSetReader.open(missingDataSrc);

        Throwable missingAttr = ThrowableCaptor.captureThrowable(() -> DataSetReader.open(missingAttrSrc));
        Throwable mismatch = ThrowableCaptor.captureThrowable(() -> DataSetReader.open(mismatchingAttributeSrc));
        Throwable misaligned = ThrowableCaptor.captureThrowable(() -> DataSetReader.open(misalignedAttributesSrc));
        Throwable unknownType = ThrowableCaptor.captureThrowable(() -> DataSetReader.open(unknownTypeSrc));

        // Assert
        List<Feature> features;

        features = ordinary.getFeatures();
        assertEquals(3, features.size());
        assertEquals(1, features.get(0).get(0), 0);
        assertEquals(4, features.get(2).get(1), 1);
        assertEquals(2, features.get(0).getAll().size());

        features = missingData.getFeatures();
        assertEquals(3, features.size());
        assertEquals(0, features.get(0).getAll().size());
        assertEquals(0, features.get(1).getAll().size());

        assertNotNull(missingAttr);
        assertEquals(RuntimeException.class, missingAttr.getClass());
        assertTrue(missingAttr.getMessage().contains("The file does not contain any attributes"));

        assertNotNull(mismatch);
        assertEquals(RuntimeException.class, mismatch.getClass());
        assertTrue(mismatch.getMessage().contains("An unexpected value was encountered"));

        assertNotNull(misaligned);
        assertEquals(RuntimeException.class, misaligned.getClass());
        assertTrue(misaligned.getMessage().contains("There are more values than attributes"));

        assertNotNull(unknownType);
        assertEquals(RuntimeException.class, unknownType.getClass());
        assertTrue(unknownType.getMessage().contains("Unrecognized data type"));
    }

}