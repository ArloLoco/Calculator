package com.example.calculator.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    var userInput by mutableStateOf("")
    var calcInput by mutableStateOf("")
    var calcComplete by mutableStateOf(false)

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Operation -> enterOperation(action.operation.symbol, action.operation.operator)
            is CalculatorAction.Delete -> delete()
            is CalculatorAction.AllClear -> allClear()
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Parentheses -> enterParentheses()
            is CalculatorAction.Percent -> percent()
            is CalculatorAction.Calculate -> calculate()
        }
    }

    // DONE
    private fun enterNumber(number: String) {
        if (calcComplete) {
            allClear()
        }
        userInput += number
        calcInput += number
        updateCalculation()
    }

    // DONE
    private fun enterOperation(symbol: String, operator: String) {
        if (userInput.isNotEmpty()) {
            val last = calcInput.takeLast(2)

            when {
                last == "+ " || last == "* " || last == "/ " || last == "- " -> {
                    delete()
                    userInput += symbol
                    calcInput += operator
                }
                last == "(" || last == ")" -> if (symbol == "-") {
                    userInput += symbol
                    calcInput += symbol
                }
                last.toDoubleOrNull() != null -> {
                    userInput += symbol
                    calcInput += operator
                    calcComplete = false
                }
                else -> { }
            }
        } else if (symbol == "-") {
            userInput += symbol
            calcInput += symbol
        }
        updateCalculation()
    }

    // DONE
    private fun delete() {
        if (userInput.isNotEmpty()) {
            userInput = userInput.dropLast(1)
            if (!calcInput.endsWith(' ')) {
                calcInput.dropLast(1)
            } else {
                calcInput.dropLast(3)
            }
            updateCalculation()
        }
    }

    // DONE
    private fun allClear() {
        calcComplete = false
        userInput = ""
        calcInput = ""
        _uiState.value = _uiState.value.copy(
            calculation = "",
            result = ""
        )
    }

    // DONE
    private fun enterDecimal() {
        if (calcComplete) {
            allClear()
        }
        if (userInput.isEmpty() || calcInput.last() == ' ') {
            userInput += "0."
            calcInput += "0."
        } else {
            userInput += "."
            calcInput += "."
        }
        updateCalculation()
    }

    // DONE
    private fun enterParentheses() {
        val countOpen = userInput.count { it == '(' }
        val countClose = userInput.count { it == ')' }
        val total = countOpen - countClose

        val openOrClose = if (total <= 0 || userInput.endsWith('(')) '(' else ')'

        calcComplete = false
        userInput += openOrClose
        calcInput += openOrClose
        updateCalculation()
    }

    // DONE
    private fun percent() {
        userInput += "%"
        calcInput += " / 100"
        updateCalculation()
    }

    //
    private fun calculate() {
        if (_uiState.value.result.isNotEmpty()) {
            userInput = _uiState.value.result
            calcInput = _uiState.value.result
            calcComplete = true
            updateCalculation()

            _uiState.value = _uiState.value.copy(
                result = ""
            )
        }
    }

    // DONE
    private fun updateCalculation() {
        _uiState.value = _uiState.value.copy(
            calculation = calcInput
        )
        resultReady()
    }

    //
    private fun resultReady() {
        val currentCalculation = _uiState.value.calculation

        if ((currentCalculation.contains(' ') || currentCalculation.contains('('))
                    &&
                    (currentCalculation.last().toString().toDoubleOrNull() != null || currentCalculation.last() == '.')) {
            _uiState.value = _uiState.value.copy(
                result = ExpressionEvaluator.evaluateExpression(currentCalculation)
            )
        }
    }
}