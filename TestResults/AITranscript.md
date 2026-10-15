Convert these into JUnit tests


Copilot said:


Below are complete JUnit 5 test files for both attached classes. They use short test names, assertThrows() for error cases, and a 0.001 tolerance for doubles.
VendingMachineItemTest.java



import static org.junit.jupiter.api.Assertions.*;
 
import org.junit.jupiter.api.Test;
 
public class VendingMachineItemTest {
 
    @Test
    void getsName() throws VendingMachineException {
        VendingMachineItem item =
                new VendingMachineItem("Chips", 1.50);
 
        assertEquals("Chips", item.getName());
    }
 
    @Test
    void getsPrice() throws VendingMachineException {
        VendingMachineItem item =
                new VendingMachineItem("Chips", 1.50);
 
        assertEquals(1.50, item.getPrice(), 0.001);
    }
 
    @Test
    void allowsZeroPrice() throws VendingMachineException {
        VendingMachineItem item =
                new VendingMachineItem("Water", 0.00);
 
        assertEquals(0.00, item.getPrice(), 0.001);
    }
 
    @Test
    void rejectsNegativePrice() {
        VendingMachineException exception = assertThrows(
                VendingMachineException.class,
                () -> new VendingMachineItem("Candy", -1.00)
        );
 
        assertEquals(
                "Price cannot be less than zero",
                exception.getMessage()
        );
    }
}
VendingMachineAITest.java






import static org.junit.jupiter.api.Assertions.*;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
 
public class VendingMachineAITest {
 
    private VendingMachineAI machine;
    private VendingMachineItem chips;
 
    @BeforeEach
    void setUp() throws VendingMachineException {
        machine = new VendingMachineAI();
        chips = new VendingMachineItem("Chips", 1.50);
    }
 
    @Test
    void startsEmpty() {
        assertEquals(0.00, machine.getBalance(), 0.001);
    }
 
    @Test
    void addsItem() throws VendingMachineException {
        machine.addItem(chips, "A");
 
        assertSame(chips, machine.getItem("A"));
    }
 
    @Test
    void addsAllSlots() throws VendingMachineException {
        VendingMachineItem soda =
                new VendingMachineItem("Soda", 2.00);
        VendingMachineItem candy =
                new VendingMachineItem("Candy", 1.00);
        VendingMachineItem water =
                new VendingMachineItem("Water", 1.25);
 
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
        VendingMachineException exception = assertThrows(
                VendingMachineException.class,
                () -> machine.addItem(chips, "E")
        );
 
        assertEquals(
                "Invalid code for vending machine item",
                exception.getMessage()
        );
    }
 
    @Test
    void rejectsOccupiedSlot() throws VendingMachineException {
        VendingMachineItem soda =
                new VendingMachineItem("Soda", 2.00);
 
        machine.addItem(chips, "A");
 
        VendingMachineException exception = assertThrows(
                VendingMachineException.class,
                () -> machine.addItem(soda, "A")
        );
 
        assertEquals(
                "Slot A already occupied",
                exception.getMessage()
        );
    }
 
    @Test
    void removesItem() throws VendingMachineException {
        machine.addItem(chips, "A");
 
        VendingMachineItem removed = machine.removeItem("A");
 
        assertSame(chips, removed);
        assertNull(machine.getItem("A"));
    }
 
    @Test
    void rejectsEmptyRemoval() {
        VendingMachineException exception = assertThrows(
                VendingMachineException.class,
                () -> machine.removeItem("A")
        );
 
        assertEquals(
                "Slot A is empty -- cannot remove item",
                exception.getMessage()
        );
    }
 
    @Test
    void insertsMoney() throws VendingMachineException {
        machine.insertMoney(2.00);
 
        assertEquals(2.00, machine.getBalance(), 0.001);
    }
 
    @Test
    void insertsMultipleAmounts() throws VendingMachineException {
        machine.insertMoney(1.00);
        machine.insertMoney(2.50);
 
        assertEquals(3.50, machine.getBalance(), 0.001);
    }
 
    @Test
    void allowsZeroAmount() throws VendingMachineException {
        machine.insertMoney(0.00);
 
        assertEquals(0.00, machine.getBalance(), 0.001);
    }
 
    @Test
    void rejectsNegativeAmount() {
        VendingMachineException exception = assertThrows(
                VendingMachineException.class,
                () -> machine.insertMoney(-1.00)
        );
 
        assertEquals(
                "Invalid amount.  Amount must be >= 0",
                exception.getMessage()
        );
        assertEquals(0.00, machine.getBalance(), 0.001);
    }
 
    @Test
    void buysWithExactMoney() throws VendingMachineException {
        machine.addItem(chips, "A");
        machine.insertMoney(1.50);
 
        boolean purchased = machine.makePurchase("A");
 
        assertTrue(purchased);
        assertEquals(0.00, machine.getBalance(), 0.001);
        assertNull(machine.getItem("A"));
    }
 
    @Test
    void buysWithExtraMoney() throws VendingMachineException {
        machine.addItem(chips, "A");
        machine.insertMoney(2.00);
 
        boolean purchased = machine.makePurchase("A");
 
        assertTrue(purchased);
        assertEquals(0.50, machine.getBalance(), 0.001);
        assertNull(machine.getItem("A"));
    }
 
    @Test
    void rejectsLowBalance() throws VendingMachineException {
        machine.addItem(chips, "A");
        machine.insertMoney(1.00);
 
        boolean purchased = machine.makePurchase("A");
 
        assertFalse(purchased);
        assertEquals(1.00, machine.getBalance(), 0.001);
        assertSame(chips, machine.getItem("A"));
    }
 
    @Test
    void rejectsEmptyPurchase() throws VendingMachineException {
        machine.insertMoney(2.00);
 
        boolean purchased = machine.makePurchase("A");
 
        assertFalse(purchased);
        assertEquals(2.00, machine.getBalance(), 0.001);
        assertNull(machine.getItem("A"));
    }
 
    @Test
    void rejectsInvalidPurchaseCode() {
        assertThrows(
                VendingMachineException.class,
                () -> machine.makePurchase("E")
        );
    }
 
    @Test
    void returnsChange() throws VendingMachineException {
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
Important: Although the attached filename is VendingMachineItemAI, the class declared inside it is VendingMachineItem. Java normally requires that file to be named VendingMachineItem.java because it contains a public class with that name.
What these tests improve
* Cover valid, invalid, and boundary inputs.
* Confirm failed purchases do not remove items or change the balance.
* Confirm successful purchases remove the purchased item.
* Check exception messages so the correct error is verified.
Use assertSame() where the exact item object should be returned.






improve AI tests for uncovered behavior


Drafting new test cases for untested code paths.
I added tests for the gaps from the earlier lists, keeping your VendingMachineAI / VendingMachineItemAI names and the throws declarations.
Created 2 files, shared files
I haven’t run these, and I kept all your original tests.
Added to VendingMachineAITest
* Startup: all four slots start empty.
* Slot checks:
    * adding to one slot leaves the others empty
    * a failed add keeps the original item
    * getItem and removeItem reject invalid codes
    * a slot can be refilled after removal
* Money:
    * a rejected negative insert keeps the existing balance
    * -0.01 is rejected
* Purchases:
    * a parameterized test at 1.49 / 1.50 / 1.51 around the chips price
    * an invalid purchase code leaves the balance unchanged
    * a free item with zero balance
    * two purchases in a row
    * a second purchase that fails when the balance runs out
    * the same item can’t be bought twice
* Change:
    * change can only be returned once
    * leftover change after a purchase
    * money can be inserted again after change is returned
Added to VendingMachineItemAITest
* Prices of 0.01 and 99.99 are accepted.
* Negative prices of -0.01, -0.50, and -100.00 are rejected.
* Two items don’t affect each other.
Check against your spec
* rejectsLowercaseCode and rejectsNullCode assume lowercase and null slot codes throw VendingMachineExceptionAI.
* If your implementation accepts lowercase or throws a NullPointerException instead, change or delete those tests.
* I didn’t add tests for a null item or null name, since the expected behavior isn’t clear from the existing tests.





Vendingmachineaitest
Code·JAVA 