import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class VendingMachineAITest {

    private static final double DELTA = 0.001;

    private VendingMachineAI machine;
    private VendingMachineItemAI chips;

    /**
     * @throws VendingMachineExceptionAI
     */
    @BeforeEach
    void setUp() throws VendingMachineExceptionAI {
        machine = new VendingMachineAI();
        chips = new VendingMachineItemAI("Chips", 1.50);
    }

    // ---------- startup ----------

    @Test
    void startsWithZeroBalance() {
        assertEquals(0.00, machine.getBalance(), DELTA);
    }

    @Test
    void startsWithAllSlotsEmpty() throws VendingMachineExceptionAI {
        assertNull(machine.getItem("A"));
        assertNull(machine.getItem("B"));
        assertNull(machine.getItem("C"));
        assertNull(machine.getItem("D"));
    }

    // ---------- addItem / getItem ----------

    @Test
    void addsItem() throws VendingMachineExceptionAI {
        machine.addItem(chips, "A");

        assertSame(chips, machine.getItem("A"));
    }

    @Test
    void addsAllSlots() throws VendingMachineExceptionAI {
        VendingMachineItemAI soda = new VendingMachineItemAI("Soda", 2.00);
        VendingMachineItemAI candy = new VendingMachineItemAI("Candy", 1.00);
        VendingMachineItemAI water = new VendingMachineItemAI("Water", 1.25);

        machine.addItem(chips, "A");
        machine.addItem(soda, "B");
        machine.addItem(candy, "C");
        machine.addItem(water, "D");

        assertSame(chips, machine.getItem("A"));
        assertSame(soda, machine.getItem("B"));
        assertSame(candy, machine.getItem("C"));
        assertSame(water, machine.getItem("D"));
    }

    @Test
    void addingToOneSlotLeavesOthersEmpty() throws VendingMachineExceptionAI {
        machine.addItem(chips, "B");

        assertNull(machine.getItem("A"));
        assertSame(chips, machine.getItem("B"));
        assertNull(machine.getItem("C"));
        assertNull(machine.getItem("D"));
    }

    @Test
    void rejectsInvalidAddCode() {
        VendingMachineExceptionAI exception = assertThrows(
                VendingMachineExceptionAI.class,
                () -> machine.addItem(chips, "E"));

        assertEquals("Invalid code for vending machine item",
                exception.getMessage());
    }

    @Test
    void rejectsOccupiedSlot() throws VendingMachineExceptionAI {
        VendingMachineItemAI soda = new VendingMachineItemAI("Soda", 2.00);
        machine.addItem(chips, "A");

        VendingMachineExceptionAI exception = assertThrows(
                VendingMachineExceptionAI.class,
                () -> machine.addItem(soda, "A"));

        assertEquals("Slot A already occupied", exception.getMessage());
    }

    @Test
    void failedAddKeepsOriginalItem() throws VendingMachineExceptionAI {
        VendingMachineItemAI soda = new VendingMachineItemAI("Soda", 2.00);
        machine.addItem(chips, "A");

        assertThrows(VendingMachineExceptionAI.class,
                () -> machine.addItem(soda, "A"));

        assertSame(chips, machine.getItem("A"));
    }

    @Test
    void rejectsInvalidGetCode() {
        assertThrows(VendingMachineExceptionAI.class,
                () -> machine.getItem("E"));
    }

    // slot codes are assumed to be case sensitive and non-null.
    // adjust these two if the spec says otherwise.
    @Test
    void rejectsLowercaseCode() {
        assertThrows(VendingMachineExceptionAI.class,
                () -> machine.addItem(chips, "a"));
        assertThrows(VendingMachineExceptionAI.class,
                () -> machine.getItem("a"));
    }

    @Test
    void rejectsNullCode() {
        assertThrows(VendingMachineExceptionAI.class,
                () -> machine.addItem(chips, null));
    }

    // ---------- removeItem ----------

    @Test
    void removesItem() throws VendingMachineExceptionAI {
        machine.addItem(chips, "A");

        VendingMachineItemAI removed = machine.removeItem("A");

        assertSame(chips, removed);
        assertNull(machine.getItem("A"));
    }

    @Test
    void slotCanBeRefilledAfterRemoval() throws VendingMachineExceptionAI {
        VendingMachineItemAI soda = new VendingMachineItemAI("Soda", 2.00);
        machine.addItem(chips, "A");
        machine.removeItem("A");

        machine.addItem(soda, "A");

        assertSame(soda, machine.getItem("A"));
    }

    @Test
    void rejectsEmptyRemoval() {
        VendingMachineExceptionAI exception = assertThrows(
                VendingMachineExceptionAI.class,
                () -> machine.removeItem("A"));

        assertEquals("Slot A is empty -- cannot remove item",
                exception.getMessage());
    }

    @Test
    void rejectsInvalidRemoveCode() {
        assertThrows(VendingMachineExceptionAI.class,
                () -> machine.removeItem("E"));
    }

    // ---------- insertMoney ----------

    @Test
    void insertsMoney() throws VendingMachineExceptionAI {
        machine.insertMoney(2.00);

        assertEquals(2.00, machine.getBalance(), DELTA);
    }

    @Test
    void insertsMultipleAmounts() throws VendingMachineExceptionAI {
        machine.insertMoney(1.00);
        machine.insertMoney(2.50);

        assertEquals(3.50, machine.getBalance(), DELTA);
    }

    @Test
    void allowsZeroAmount() throws VendingMachineExceptionAI {
        machine.insertMoney(0.00);

        assertEquals(0.00, machine.getBalance(), DELTA);
    }

    @Test
    void rejectsNegativeAmount() {
        VendingMachineExceptionAI exception = assertThrows(
                VendingMachineExceptionAI.class,
                () -> machine.insertMoney(-1.00));

        assertEquals("Invalid amount.  Amount must be >= 0",
                exception.getMessage());
        assertEquals(0.00, machine.getBalance(), DELTA);
    }

    @Test
    void rejectsSmallestNegativeAmount() {
        assertThrows(VendingMachineExceptionAI.class,
                () -> machine.insertMoney(-0.01));
    }

    @Test
    void rejectedInsertKeepsExistingBalance() throws VendingMachineExceptionAI {
        machine.insertMoney(3.00);

        assertThrows(VendingMachineExceptionAI.class,
                () -> machine.insertMoney(-1.00));

        assertEquals(3.00, machine.getBalance(), DELTA);
    }

    // ---------- makePurchase ----------

    @Test
    void buysWithExactMoney() throws VendingMachineExceptionAI {
        machine.addItem(chips, "A");
        machine.insertMoney(1.50);

        assertTrue(machine.makePurchase("A"));
        assertEquals(0.00, machine.getBalance(), DELTA);
        assertNull(machine.getItem("A"));
    }

    @Test
    void buysWithExtraMoney() throws VendingMachineExceptionAI {
        machine.addItem(chips, "A");
        machine.insertMoney(2.00);

        assertTrue(machine.makePurchase("A"));
        assertEquals(0.50, machine.getBalance(), DELTA);
        assertNull(machine.getItem("A"));
    }

    // chips cost 1.50, so this checks just below, at, and just above the price
    @ParameterizedTest
    @CsvSource({
        "0.00, false, 0.00",
        "1.00, false, 1.00",
        "1.49, false, 1.49",
        "1.50, true,  0.00",
        "1.51, true,  0.01",
        "5.00, true,  3.50"
    })
    void purchaseDependsOnBalance(double balance, boolean expectedResult,
            double expectedBalance) throws VendingMachineExceptionAI {
        machine.addItem(chips, "A");
        machine.insertMoney(balance);

        assertEquals(expectedResult, machine.makePurchase("A"));
        assertEquals(expectedBalance, machine.getBalance(), DELTA);
        assertEquals(!expectedResult, machine.getItem("A") != null);
    }

    @Test
    void rejectsLowBalance() throws VendingMachineExceptionAI {
        machine.addItem(chips, "A");
        machine.insertMoney(1.00);

        assertFalse(machine.makePurchase("A"));
        assertEquals(1.00, machine.getBalance(), DELTA);
        assertSame(chips, machine.getItem("A"));
    }

    @Test
    void rejectsEmptyPurchase() throws VendingMachineExceptionAI {
        machine.insertMoney(2.00);

        assertFalse(machine.makePurchase("A"));
        assertEquals(2.00, machine.getBalance(), DELTA);
        assertNull(machine.getItem("A"));
    }

    @Test
    void rejectsInvalidPurchaseCode() throws VendingMachineExceptionAI {
        machine.insertMoney(2.00);

        assertThrows(VendingMachineExceptionAI.class,
                () -> machine.makePurchase("E"));

        assertEquals(2.00, machine.getBalance(), DELTA);
    }

    @Test
    void buysFreeItemWithZeroBalance() throws VendingMachineExceptionAI {
        VendingMachineItemAI sample = new VendingMachineItemAI("Sample", 0.00);
        machine.addItem(sample, "A");

        assertTrue(machine.makePurchase("A"));
        assertNull(machine.getItem("A"));
        assertEquals(0.00, machine.getBalance(), DELTA);
    }

    @Test
    void buysTwoItemsInARow() throws VendingMachineExceptionAI {
        VendingMachineItemAI soda = new VendingMachineItemAI("Soda", 2.00);
        machine.addItem(chips, "A");
        machine.addItem(soda, "B");
        machine.insertMoney(4.00);

        assertTrue(machine.makePurchase("A"));
        assertEquals(2.50, machine.getBalance(), DELTA);

        assertTrue(machine.makePurchase("B"));
        assertEquals(0.50, machine.getBalance(), DELTA);

        assertNull(machine.getItem("A"));
        assertNull(machine.getItem("B"));
    }

    @Test
    void secondPurchaseFailsWhenBalanceRunsOut() throws VendingMachineExceptionAI {
        VendingMachineItemAI soda = new VendingMachineItemAI("Soda", 2.00);
        machine.addItem(chips, "A");
        machine.addItem(soda, "B");
        machine.insertMoney(2.00);

        assertTrue(machine.makePurchase("A"));
        assertFalse(machine.makePurchase("B"));

        assertEquals(0.50, machine.getBalance(), DELTA);
        assertSame(soda, machine.getItem("B"));
    }

    @Test
    void cannotBuySameItemTwice() throws VendingMachineExceptionAI {
        machine.addItem(chips, "A");
        machine.insertMoney(5.00);

        assertTrue(machine.makePurchase("A"));
        assertFalse(machine.makePurchase("A"));

        assertEquals(3.50, machine.getBalance(), DELTA);
    }

    // ---------- returnChange ----------

    @Test
    void returnsChange() throws VendingMachineExceptionAI {
        machine.insertMoney(2.00);

        assertEquals(2.00, machine.returnChange(), DELTA);
        assertEquals(0.00, machine.getBalance(), DELTA);
    }

    @Test
    void returnsZeroChange() {
        assertEquals(0.00, machine.returnChange(), DELTA);
        assertEquals(0.00, machine.getBalance(), DELTA);
    }

    @Test
    void returnsChangeOnlyOnce() throws VendingMachineExceptionAI {
        machine.insertMoney(2.00);
        machine.returnChange();

        assertEquals(0.00, machine.returnChange(), DELTA);
    }

    @Test
    void returnsLeftoverAfterPurchase() throws VendingMachineExceptionAI {
        machine.addItem(chips, "A");
        machine.insertMoney(2.00);
        machine.makePurchase("A");

        assertEquals(0.50, machine.returnChange(), DELTA);
        assertEquals(0.00, machine.getBalance(), DELTA);
    }

    @Test
    void canInsertMoneyAfterReturningChange() throws VendingMachineExceptionAI {
        machine.insertMoney(2.00);
        machine.returnChange();
        machine.insertMoney(1.00);

        assertEquals(1.00, machine.getBalance(), DELTA);
    }
}