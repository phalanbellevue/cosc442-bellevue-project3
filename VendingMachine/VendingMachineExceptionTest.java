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
        VendingMachineExceptionAI exception =
                new VendingMachineExceptionAI();

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
        VendingMachineExceptionAI exception =
                new VendingMachineExceptionAI(message);

        // Assert
        assertEquals(message, exception.getMessage());
    }
}