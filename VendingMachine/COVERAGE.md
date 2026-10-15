## Initial Coverage Results

Had 100% Coverage in Project 2 for VendingMachine, no additional test cases were needed

Vending Machine Coverage:

⁃	43/43 (100%) Statments Covered
⁃	9/9 (100%) Functions Covered
⁃	20/20 (100%) Branches Covered


## Final Coverage Results

Vending Machine Coverage:

⁃	43/43 (100%) Statments Covered
⁃	9/9 (100%) Functions Covered
⁃	20/20 (100%) Branches Covered


## Condition Check for makePurchase():

C1 true: 
⁃	purchaseWithLowBalanceFails
⁃	purchaseWithExactBalanceSucceeds
⁃	purchaseRemovesItem
⁃	purchaseDeductsPrice
⁃	all purchaseUsesBalance csv rows 

C1 false:
⁃	purchaseFromEmptySlotFails,where slot “A” is never filled


C2 true:
⁃	purchaseWithExactBalanceSucceeds
⁃	purchaseRemovesItem
⁃	purchaseDeductsPrice
⁃	2.00, 2.01, and 5.00 csv rows of purchaseUsesBalance 

C2 false: 
⁃	0.00, 0.50, and 1.99 csv rows of purchaseWithLowBalanceFails


## Test-Sensitivity Experiment

if ((item != null) && (this.balance == item.getPrice())) // INJECTED FAULT FOR COVERAGE EXPERIMENT

Failed Tests:

⁃	purchaseDeductsPrice()
⁃	purchaseUsesBalance(double, boolean, double)
    - Test Case: 2.01, true, 0.01
    - Test Case: 5.00, true, 3.00


