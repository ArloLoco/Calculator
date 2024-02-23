package com.example.calculator.ui

data class CalculatorUiState(
    val calculation: String = "",
    val result: String= "",
    val operation: CalculatorOperation? = null
)