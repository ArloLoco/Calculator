package com.example.calculator.ui

sealed class CalculatorAction {
    data class Number(val number: String): CalculatorAction()
    data class Operation(val operation: CalculatorOperation): CalculatorAction()
    object AllClear: CalculatorAction()
    object Delete: CalculatorAction()
    object Parentheses: CalculatorAction()
    object Percent: CalculatorAction()
    object Decimal: CalculatorAction()
    object Calculate: CalculatorAction()
}