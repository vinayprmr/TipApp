package com.example.tipapp.utils


fun calculateTotalTip(totalBill: Double, tipPercentage: Int): Double {
    return if (totalBill > 1 && totalBill.toString()
            .isNotEmpty()
    ) (totalBill * tipPercentage) / 100 else 0.0
}

fun calculateTotalPerPerson(tipAmount: Double, personsToSplit: Int, billAmount: Int): Double {
    return (billAmount + tipAmount) / personsToSplit
}
