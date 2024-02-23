package com.example.calculator.ui

sealed class CalculatorOperation(
    val symbol: String,
    val operator: String
) {
    data object Add: CalculatorOperation("+", " + ")
    data object Minus: CalculatorOperation("-", " - ")
    data object Multiply: CalculatorOperation("\u00D7"," * ")
    data object Divide: CalculatorOperation("\u00F7"," / ")
}