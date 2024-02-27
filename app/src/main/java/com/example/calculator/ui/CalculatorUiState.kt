package com.example.calculator.ui

data class CalculatorUiState(
    val userInput: String = "",
    val calculation: String = "",
    val result: String= "",
    val operation: CalculatorOperation? = null,
    val isCalculationComplete: Boolean = false
)