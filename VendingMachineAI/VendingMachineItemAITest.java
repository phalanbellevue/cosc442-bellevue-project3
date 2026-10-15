import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class VendingMachineItemAITest {

    private static final double DELTA = 0.001;

    @Test
    void getsName() throws VendingMachineExceptionAI {
        VendingMachineItemAI item = new VendingMachineItemAI("Chips", 1.50);

        assertEquals("Chips", item.getName());
    }

    @Test
    void getsPrice() throws VendingMachineExceptionAI {
        VendingMachineItemAI item = new VendingMachineItemAI("Chips", 1.50);

        assertEquals(1.50, item.getPrice(), DELTA);
    }

    @Test
    void allowsZeroPrice() throws VendingMachineExceptionAI {
        VendingMachineItemAI item = new VendingMachineItemAI("Water", 0.00);

        assertEquals(0.00, item.getPrice(), DELTA);
    }

    @Test
    void allowsSmallestPositivePrice() throws VendingMachineExceptionAI {
        VendingMachineItemAI item = new VendingMachineItemAI("Gum", 0.01);

        assertEquals(0.01, item.getPrice(), DELTA);
    }

    @Test
    void allowsLargePrice() throws VendingMachineExceptionAI {
        VendingMachineItemAI item = new VendingMachineItemAI("Combo", 99.99);

        assertEquals(99.99, item.getPrice(), DELTA);
    }

    @Test
    void rejectsNegativePrice() {
        VendingMachineExceptionAI exception = assertThrows(
                VendingMachineExceptionAI.class,
                () -> new VendingMachineItemAI("Candy", -1.00));

        assertEquals("Price cannot be less than zero", exception.getMessage());
    }

    // just below zero, a small negative, and a large negative
    @ParameterizedTest
    @ValueSource(doubles = {-0.01, -0.50, -100.00})
    void rejectsAnyNegativePrice(double price) {
        assertThrows(VendingMachineExceptionAI.class,
                () -> new VendingMachineItemAI("Candy", price));
    }

    @Test
    void itemsAreIndependent() throws VendingMachineExceptionAI {
        VendingMachineItemAI chips = new VendingMachineItemAI("Chips", 1.50);
        VendingMachineItemAI soda = new VendingMachineItemAI("Soda", 2.00);

        assertEquals("Chips", chips.getName());
        assertEquals(1.50, chips.getPrice(), DELTA);
        assertEquals("Soda", soda.getName());
        assertEquals(2.00, soda.getPrice(), DELTA);
    }
}