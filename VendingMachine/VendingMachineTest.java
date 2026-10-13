import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for the VendingMachine class.
 */
public class VendingMachineTest {

    private static final double DELTA = 0.001;

    private VendingMachine machine;

    /**
     * Creates a new vending machine before each test.
     */
    @BeforeEach
    public void setUp() {
        machine = new VendingMachine();
    }

    /**
     * Tests that a new machine has a zero balance.
     */
    @Test
    public void newMachineHasZeroBalance() {
        // Arrange and Act are completed in setUp()

        // Assert
        assertEquals(0.0, machine.getBalance(), DELTA);
    }

    /**
     * Tests that all slots start empty.
     */
    @Test
    public void newMachineHasEmptySlots() {
        // Arrange and Act are completed in setUp()

        // Assert
        assertNull(machine.getItem("A"));
        assertNull(machine.getItem("B"));
        assertNull(machine.getItem("C"));
        assertNull(machine.getItem("D"));
    }

    /**
     * Tests adding an item to an empty slot.
     */
    @Test
    public void addItemStoresItem() {
        // Arrange
        VendingMachineItem item =
                new VendingMachineItem("Chips", 1.50);

        // Act
        machine.addItem(item, "A");

        // Assert
        assertSame(item, machine.getItem("A"));
    }

    /**
     * Tests adding an item to an occupied slot.
     */
    @Test
    public void addItemToFullSlotThrowsException() {
        // Arrange
        VendingMachineItem firstItem =
                new VendingMachineItem("Chips", 1.50);
        VendingMachineItem secondItem =
                new VendingMachineItem("Soda", 2.00);

        machine.addItem(firstItem, "A");

        // Act and Assert
        assertThrows(
                VendingMachineException.class,
                () -> machine.addItem(secondItem, "A"));
    }

    /**
     * Tests adding an item with an invalid slot code.
     */
    @Test
    public void addItemWithBadCodeThrowsException() {
        // Arrange
        VendingMachineItem item =
                new VendingMachineItem("Chips", 1.50);

        // Act and Assert
        assertThrows(
                VendingMachineException.class,
                () -> machine.addItem(item, "E"));
    }

    /**
     * Tests getting an item from an occupied slot.
     */
    @Test
    public void getItemReturnsItem() {
        // Arrange
        VendingMachineItem item =
                new VendingMachineItem("Water", 1.00);

        machine.addItem(item, "B");

        // Act
        VendingMachineItem result = machine.getItem("B");

        // Assert
        assertSame(item, result);
    }

    /**
     * Tests getting an item with an invalid code.
     */
    @Test
    public void getItemWithBadCodeThrowsException() {
        // Arrange
        String invalidCode = "E";

        // Act and Assert
        assertThrows(
                VendingMachineException.class,
                () -> machine.getItem(invalidCode));
    }

    /**
     * Tests removing an item from an occupied slot.
     */
    @Test
    public void removeItemReturnsItem() {
        // Arrange
        VendingMachineItem item =
                new VendingMachineItem("Candy", 1.25);

        machine.addItem(item, "C");

        // Act
        VendingMachineItem result = machine.removeItem("C");

        // Assert
        assertSame(item, result);
    }

    /**
     * Tests that removing an item empties the slot.
     */
    @Test
    public void removeItemEmptiesSlot() {
        // Arrange
        VendingMachineItem item =
                new VendingMachineItem("Candy", 1.25);

        machine.addItem(item, "C");

        // Act
        machine.removeItem("C");

        // Assert
        assertNull(machine.getItem("C"));
    }

    /**
     * Tests removing an item from an empty slot.
     */
    @Test
    public void removeFromEmptySlotThrowsException() {
        // Arrange
        String emptyCode = "D";

        // Act and Assert
        assertThrows(
                VendingMachineException.class,
                () -> machine.removeItem(emptyCode));
    }

    /**
     * Tests inserting a positive amount.
     */
    @Test
    public void insertMoneyAddsBalance() {
        // Arrange
        double amount = 5.00;

        // Act
        machine.insertMoney(amount);

        // Assert
        assertEquals(5.00, machine.getBalance(), DELTA);
    }

    /**
     * Tests inserting zero, which is the minimum valid amount.
     */
    @Test
    public void insertZeroIsAllowed() {
        // Arrange
        double amount = 0.0;

        // Act
        machine.insertMoney(amount);

        // Assert
        assertEquals(0.0, machine.getBalance(), DELTA);
    }

    /**
     * Tests inserting a negative amount.
     */
    @Test
    public void insertNegativeThrowsException() {
        // Arrange
        double amount = -0.01;

        // Act and Assert
        assertThrows(
                VendingMachineException.class,
                () -> machine.insertMoney(amount));
    }

    /**
     * Tests getting the current balance.
     */
    @Test
    public void getBalanceReturnsBalance() {
        // Arrange
        machine.insertMoney(4.50);

        // Act
        double balance = machine.getBalance();

        // Assert
        assertEquals(4.50, balance, DELTA);
    }

    /**
     * Tests a purchase with insufficient money.
     */
    @Test
    public void purchaseWithLowBalanceFails() {
        // Arrange
        VendingMachineItem item =
                new VendingMachineItem("Soda", 2.00);

        machine.addItem(item, "A");
        machine.insertMoney(1.00);

        // Act
        boolean result = machine.makePurchase("A");

        // Assert
        assertFalse(result);
    }

    /**
     * Tests a purchase using the exact item price.
     */
    @Test
    public void purchaseWithExactBalanceSucceeds() {
        // Arrange
        VendingMachineItem item =
                new VendingMachineItem("Soda", 2.00);

        machine.addItem(item, "A");
        machine.insertMoney(2.00);

        // Act
        boolean result = machine.makePurchase("A");

        // Assert
        assertTrue(result);
    }

    /**
     * Tests that a successful purchase removes the item.
     */
    @Test
    public void purchaseRemovesItem() {
        // Arrange
        VendingMachineItem item =
                new VendingMachineItem("Soda", 2.00);

        machine.addItem(item, "A");
        machine.insertMoney(2.00);

        // Act
        machine.makePurchase("A");

        // Assert
        assertNull(machine.getItem("A"));
    }

    /**
     * Tests that a purchase deducts the item price.
     */
    @Test
    public void purchaseDeductsPrice() {
        // Arrange
        VendingMachineItem item =
                new VendingMachineItem("Soda", 2.00);

        machine.addItem(item, "A");
        machine.insertMoney(5.00);

        // Act
        machine.makePurchase("A");

        // Assert
        assertEquals(3.00, machine.getBalance(), DELTA);
    }

    /**
     * Tests purchasing from an empty slot.
     */
    @Test
    public void purchaseFromEmptySlotFails() {
        // Arrange
        machine.insertMoney(5.00);

        // Act
        boolean result = machine.makePurchase("A");

        // Assert
        assertFalse(result);
    }

    /**
     * Tests balances below, equal to, and above the item price.
     */
    @ParameterizedTest
    @CsvSource({
        "0.00, false, 0.00",
        "0.50, false, 0.50",
        "1.99, false, 1.99",
        "2.00, true, 0.00",
        "2.01, true, 0.01",
        "5.00, true, 3.00"
    })
    public void purchaseUsesBalance(
            double balance,
            boolean expectedResult,
            double expectedBalance) {

        // Arrange
        VendingMachineItem item =
                new VendingMachineItem("Soda", 2.00);

        machine.addItem(item, "B");
        machine.insertMoney(balance);

        // Act
        boolean result = machine.makePurchase("B");

        // Assert
        assertEquals(expectedResult, result);
        assertEquals(expectedBalance, machine.getBalance(), DELTA);
    }

    /**
     * Tests returning the current balance as change.
     */
    @Test
    public void returnChangeReturnsBalance() {
        // Arrange
        machine.insertMoney(5.75);

        // Act
        double change = machine.returnChange();

        // Assert
        assertEquals(5.75, change, DELTA);
    }

    /**
     * Tests that returning change clears the balance.
     */
    @Test
    public void returnChangeClearsBalance() {
        // Arrange
        machine.insertMoney(5.75);

        // Act
        machine.returnChange();

        // Assert
        assertEquals(0.0, machine.getBalance(), DELTA);
    }

    /**
     * Tests returning change when the balance is zero.
     */
    @Test
    public void returnChangeFromZeroReturnsZero() {
        // Arrange
        double expectedChange = 0.0;

        // Act
        double change = machine.returnChange();

        // Assert
        assertEquals(expectedChange, change, DELTA);
    }
}
