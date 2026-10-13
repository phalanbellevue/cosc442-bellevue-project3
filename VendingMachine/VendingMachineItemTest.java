import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for the VendingMachineItem class.
 */
public class VendingMachineItemTest {

    private static final double DELTA = 0.001;

    /**
     * Tests creating an item with a valid price.
     */
    @Test
    public void validPriceCreatesItem() {
        // Arrange
        String name = "Chips";
        double price = 1.50;

        // Act
        VendingMachineItem item =
                new VendingMachineItem(name, price);

        // Assert
        assertEquals("Chips", item.getName());
        assertEquals(1.50, item.getPrice(), DELTA);
    }

    /**
     * Tests creating an item with a price of zero.
     */
    @Test
    public void zeroPriceIsAllowed() {
        // Arrange
        String name = "Free Sample";
        double price = 0.0;

        // Act
        VendingMachineItem item =
                new VendingMachineItem(name, price);

        // Assert
        assertEquals(0.0, item.getPrice(), DELTA);
    }

    /**
     * Tests creating an item with a negative price.
     */
    @Test
    public void negativePriceThrowsException() {
        // Arrange
        String name = "Invalid Item";
        double price = -0.01;

        // Act and Assert
        assertThrows(
                VendingMachineException.class,
                () -> new VendingMachineItem(name, price));
    }

    /**
     * Tests getting the item's name.
     */
    @Test
    public void getNameReturnsName() {
        // Arrange
        VendingMachineItem item =
                new VendingMachineItem("Water", 1.25);

        // Act
        String name = item.getName();

        // Assert
        assertEquals("Water", name);
    }

    /**
     * Tests getting the item's price.
     */
    @Test
    public void getPriceReturnsPrice() {
        // Arrange
        VendingMachineItem item =
                new VendingMachineItem("Sandwich", 2.75);

        // Act
        double price = item.getPrice();

        // Assert
        assertEquals(2.75, price, DELTA);
    }
}
