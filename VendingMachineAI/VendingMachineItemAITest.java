import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class VendingMachineItemAITest {

    /** 
     * @throws VendingMachineExceptionAI
     */
    @Test
    void getsName() throws VendingMachineExceptionAI {
        VendingMachineItemAI item =
                new VendingMachineItemAI("Chips", 1.50);

        assertEquals("Chips", item.getName());
    }

    /** 
     * @throws VendingMachineExceptionAI
     */
    @Test
    void getsPrice() throws VendingMachineExceptionAI {
        VendingMachineItemAI item =
                new VendingMachineItemAI("Chips", 1.50);

        assertEquals(1.50, item.getPrice(), 0.001);
    }

    /** 
     * @throws VendingMachineExceptionAI
     */
    @Test
    void allowsZeroPrice() throws VendingMachineExceptionAI {
        VendingMachineItemAI item =
                new VendingMachineItemAI("Water", 0.00);

        assertEquals(0.00, item.getPrice(), 0.001);
    }

    @Test
    void rejectsNegativePrice() {
        VendingMachineExceptionAI exception = assertThrows(
                VendingMachineExceptionAI.class,
                () -> new VendingMachineItemAI("Candy", -1.00)
        );

        assertEquals(
                "Price cannot be less than zero",
                exception.getMessage()
        );
    }
}