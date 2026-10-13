# TEST PLAN

## VendingMachine

| Method / Behavior | Valid Case(s) | Invalid Case(s) | Boundary Case(s) | Expected Result / Oracle | Related Test(s) |
|---|---|---|---|---|---|
| Constructor | Create a machine | N/A | Initial state | Javadocs say balance is 0 and all slots are empty | newMachineHasZeroBalance, newMachineHasEmptySlots |
| addItem() | Add to empty slot | Full slot or invalid code | Valid codes A-D | Javadocs say the item is stored in the selected slot | addItemStoresItem, addItemToFullSlotThrowsException, addItemWithBadCodeThrowsException |
| getItem() | Get stored item | Invalid code | Empty slot | Javadocs say the item in the slot is returned | getItemReturnsItem, getItemWithBadCodeThrowsException |
| removeItem() | Remove stored item | Empty slot | Empty vs. occupied slot | Javadocs say the item is removed and returned | removeItemReturnsItem, removeItemEmptiesSlot, removeFromEmptySlotThrowsException |
| insertMoney() | Insert positive money | Negative amount | 0 and -0.01 | Javadocs say amounts greater than or equal to 0 are valid | insertMoneyAddsBalance, insertZeroIsAllowed, insertNegativeThrowsException |
| getBalance() | Get current balance | N/A | Balance of 0 | Javadocs say the current balance is returned | getBalanceReturnsBalance |
| makePurchase() | Purchase with enough money | Empty slot or low balance | Below, equal to, and above price | Javadocs say a purchase succeeds when balance is at least the price | purchaseWithLowBalanceFails, purchaseWithExactBalanceSucceeds, purchaseRemovesItem, purchaseDeductsPrice, purchaseFromEmptySlotFails, purchaseUsesBalance |
| returnChange() | Return positive balance | N/A | Balance of 0 | Javadocs say the balance is returned and reset to 0 | returnChangeReturnsBalance, returnChangeClearsBalance, returnChangeFromZeroReturnsZero |

## VendingMachineItem

| Method / Behavior | Valid Case(s) | Invalid Case(s) | Boundary Case(s) | Expected Result / Oracle | Related Test(s) |
|---|---|---|---|---|---|
| Constructor | Positive price | Negative price | Price of 0 and -0.01 | Javadocs say price must be greater than or equal to 0 | validPriceCreatesItem, zeroPriceIsAllowed, negativePriceThrowsException |
| getName() | Get item name | N/A | N/A | Javadocs say the stored name is returned | getNameReturnsName |
| getPrice() | Get item price | N/A | Price of 0 | Javadocs say the stored price is returned | getPriceReturnsPrice |

## VendingMachineException

| Method / Behavior | Valid Case(s) | Invalid Case(s) | Boundary Case(s) | Expected Result / Oracle | Related Test(s) |
|---|---|---|---|---|---|
| Constructors | Create with or without a message | N/A | N/A | The constructor should store the supplied message | defaultExceptionHasNoMessage, exceptionStoresMessage |

## Parameterized Test

`purchaseUsesBalance` is the parameterized test.

It uses six balances:

- 0.00
- 0.50
- 1.99
- 2.00
- 2.01
- 5.00

The item price is $2.00. The values test balances below the price, exactly
equal to the price, and above the price. This tests the boundary where a
purchase changes from failing to succeeding.
