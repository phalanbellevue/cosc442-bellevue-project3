import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VendingMachineAITest {

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

    @Test
    void startsEmpty() {
        assertEquals(0.00, machine.getBalance(), 0.001);
    }

    /** 
     * @throws VendingMachineExceptionAI
     */
    @Test
    void addsItem() throws VendingMachineExceptionAI {
        machine.addItem(chips, "A");

        assertSame(chips, machine.getItem("A"));
    }

    /** 
     * @throws VendingMachineExceptionAI
     */
    @Test
    void addsAllSlots() throws VendingMachineExceptionAI {
        VendingMachineItemAI soda =
                new VendingMachineItemAI("Soda", 2.00);
        VendingMachineItemAI candy =
                new VendingMachineItemAI("Candy", 1.00);
        VendingMachineItemAI water =
                new VendingMachineItemAI("Water", 1.25);

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
    void rejectsInvalidCode() {
        VendingMachineExceptionAI exception = assertThrows(
                VendingMachineExceptionAI.class,
                () -> machine.addItem(chips, "E")
        );

        assertEquals(
                "Invalid code for vending machine item",
                exception.getMessage()
        );
    }

    /** 
     * @throws VendingMachineExceptionAI
     */
    @Test
    void rejectsOccupiedSlot() throws VendingMachineExceptionAI {
        VendingMachineItemAI soda =
                new VendingMachineItemAI("Soda", 2.00);

        machine.addItem(chips, "A");

        VendingMachineExceptionAI exception = assertThrows(
                VendingMachineExceptionAI.class,
                () -> machine.addItem(soda, "A")
        );

        assertEquals(
                "Slot A already occupied",
                exception.getMessage()
        );
    }

    /** 
     * @throws VendingMachineExceptionAI
     */
    @Test
    void removesItem() throws VendingMachineExceptionAI {
        machine.addItem(chips, "A");

        VendingMachineItemAI removed = machine.removeItem("A");

        assertSame(chips, removed);
        assertNull(machine.getItem("A"));
    }

    @Test
    void rejectsEmptyRemoval() {
        VendingMachineExceptionAI exception = assertThrows(
                VendingMachineExceptionAI.class,
                () -> machine.removeItem("A")
        );

        assertEquals(
                "Slot A is empty -- cannot remove item",
                exception.getMessage()
        );
    }

    /** 
     * @throws VendingMachineExceptionAI
     */
    @Test
    void insertsMoney() throws VendingMachineExceptionAI {
        machine.insertMoney(2.00);

        assertEquals(2.00, machine.getBalance(), 0.001);
    }

    /** 
     * @throws VendingMachineExceptionAI
     */
    @Test
    void insertsMultipleAmounts() throws VendingMachineExceptionAI {
        machine.insertMoney(1.00);
        machine.insertMoney(2.50);

        assertEquals(3.50, machine.getBalance(), 0.001);
    }

    /** 
     * @throws VendingMachineExceptionAI
     */
    @Test
    void allowsZeroAmount() throws VendingMachineExceptionAI {
        machine.insertMoney(0.00);

        assertEquals(0.00, machine.getBalance(), 0.001);
    }

    @Test
    void rejectsNegativeAmount() {
        VendingMachineExceptionAI exception = assertThrows(
                VendingMachineExceptionAI.class,
                () -> machine.insertMoney(-1.00)
        );

        assertEquals(
                "Invalid amount.  Amount must be >= 0",
                exception.getMessage()
        );

        assertEquals(0.00, machine.getBalance(), 0.001);
    }

    /** 
     * @throws VendingMachineExceptionAI
     */
    @Test
    void buysWithExactMoney() throws VendingMachineExceptionAI {
        machine.addItem(chips, "A");
        machine.insertMoney(1.50);

        boolean purchased = machine.makePurchase("A");

        assertTrue(purchased);
        assertEquals(0.00, machine.getBalance(), 0.001);
        assertNull(machine.getItem("A"));
    }

    /** 
     * @throws VendingMachineExceptionAI
     */
    @Test
    void buysWithExtraMoney() throws VendingMachineExceptionAI {
        machine.addItem(chips, "A");
        machine.insertMoney(2.00);

        boolean purchased = machine.makePurchase("A");

        assertTrue(purchased);
        assertEquals(0.50, machine.getBalance(), 0.001);
        assertNull(machine.getItem("A"));
    }

    /** 
     * @throws VendingMachineExceptionAI
     */
    @Test
    void rejectsLowBalance() throws VendingMachineExceptionAI {
        machine.addItem(chips, "A");
        machine.insertMoney(1.00);

        boolean purchased = machine.makePurchase("A");

        assertFalse(purchased);
        assertEquals(1.00, machine.getBalance(), 0.001);
        assertSame(chips, machine.getItem("A"));
    }

    /** 
     * @throws VendingMachineExceptionAI
     */
    @Test
    void rejectsEmptyPurchase() throws VendingMachineExceptionAI {
        machine.insertMoney(2.00);

        boolean purchased = machine.makePurchase("A");

        assertFalse(purchased);
        assertEquals(2.00, machine.getBalance(), 0.001);
        assertNull(machine.getItem("A"));
    }

    @Test
    void rejectsInvalidPurchaseCode() {
        assertThrows(
                VendingMachineExceptionAI.class,
                () -> machine.makePurchase("E")
        );
    }

    /** 
     * @throws VendingMachineExceptionAI
     */
    @Test
    void returnsChange() throws VendingMachineExceptionAI {
        machine.insertMoney(2.00);

        double change = machine.returnChange();

        assertEquals(2.00, change, 0.001);
        assertEquals(0.00, machine.getBalance(), 0.001);
    }

    @Test
    void returnsZeroChange() {
        double change = machine.returnChange();

        assertEquals(0.00, change, 0.001);
        assertEquals(0.00, machine.getBalance(), 0.001);
    }
}