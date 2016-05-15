package base;

import org.junit.Test;

import static org.junit.Assert.*;

public class ContextTest {

    private static final String TAG = "TAG", NOT_TAG = "NOT_TAG";

    @Test
    public void testAdd() throws Exception {
        // Arrange
        Context context = new Context();

        // Act
        boolean normal = context.add(TAG, "Test");
        boolean duplicate = context.add(TAG, "TestTwo");

        // Assert
        assertTrue(normal);
        assertFalse(duplicate);
    }

    @Test
    public void testGet() throws Exception {
        // Arrange
        Context context = new Context();
        context.add(TAG, "Test");

        // Act
        String test = (String) context.get(TAG);
        String notExist = (String) context.get(NOT_TAG);

        // Assert
        assertEquals("Test", test);
        assertEquals(null, notExist);
    }

    @Test
    public void testHas() throws Exception {
        // Arrange
        Context context = new Context();
        context.add(TAG, "Test");

        // Act
        boolean exists = context.has(TAG);
        boolean notExist = context.has(NOT_TAG);

        // Assert
        assertTrue(exists);
        assertFalse(notExist);
    }
}