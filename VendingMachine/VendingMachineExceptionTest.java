import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Tests for the VendingMachineException class.
 */
public class VendingMachineExceptionTest {

    /**
     * Tests creating an exception without a message.
     */
    @Test
    public void defaultExceptionHasNoMessage() {
        // Arrange and Act
        VendingMachineException exception =
                new VendingMachineException();

        // Assert
        assertNull(exception.getMessage());
    }

    /**
     * Tests creating an exception with a message.
     */
    @Test
    public void exceptionStoresMessage() {
        // Arrange
        String message = "Invalid operation";

        // Act
        VendingMachineException exception =
                new VendingMachineException(message);

        // Assert
        assertEquals(message, exception.getMessage());
    }
}