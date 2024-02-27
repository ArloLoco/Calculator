package com.example.calculator.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

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
        if (_uiState.value.isCalculationComplete) {
            allClear()
        }
        _uiState.value.userInput + number
        _uiState.value.calculation + number
        resultReady()
    }

    // DONE
    private fun enterOperation(symbol: String, operator: String) {
        if (_uiState.value.userInput.isNotEmpty()) {
            val last = _uiState.value.calculation.takeLast(2)

            when {
                last == "+ " || last == "* " || last == "/ " || last == "- " -> {
                    delete()
                    _uiState.value.userInput + symbol
                    _uiState.value.calculation + operator
                }
                last == "(" || last == ")" -> if (symbol == "-") {
                    _uiState.value.userInput + symbol
                    _uiState.value.calculation + symbol
                }
                last.toDoubleOrNull() != null -> {
                    _uiState.value.userInput + symbol
                    _uiState.value.calculation + operator
                    _uiState.value = _uiState.value.copy(
                        isCalculationComplete = false
                    )
                }
                else -> { }
            }
        } else if (symbol == "-") {
            _uiState.value.userInput + symbol
            _uiState.value.calculation + symbol
        }
        resultReady()
    }

    // DONE
    private fun delete() {
        if (_uiState.value.userInput.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(
                userInput = _uiState.value.userInput.dropLast(1)
            )


            if (!_uiState.value.calculation.endsWith(' ')) {
                _uiState.value.calculation.dropLast(1)
            } else {
                _uiState.value.calculation.dropLast(3)
            }
            resultReady()
        }
    }

    // DONE
    private fun allClear() {
        _uiState.value = _uiState.value.copy(
            userInput = "",
            calculation = "",
            result = "",
            isCalculationComplete = false
        )
    }

    // DONE
    private fun enterDecimal() {
        if (_uiState.value.isCalculationComplete) {
            allClear()
        }
        if (_uiState.value.userInput.isEmpty() || _uiState.value.calculation.last() == ' ') {
            _uiState.value = _uiState.value.copy(
                userInput = "0.",
                calculation = "0."
            )
        } else {
            _uiState.value.userInput + "."
            _uiState.value.calculation + "."
        }
        resultReady()
    }

    // DONE
    private fun enterParentheses() {
        val countOpen = _uiState.value.userInput.count { it == '(' }
        val countClose = _uiState.value.userInput.count { it == ')' }
        val total = countOpen - countClose

        val openOrClose = if (total <= 0 || _uiState.value.userInput.endsWith('(')) '(' else ')'

        _uiState.value = _uiState.value.copy(
            isCalculationComplete = false
        )

        _uiState.value.userInput + openOrClose
        _uiState.value.calculation + openOrClose
        resultReady()
    }

    // DONE
    private fun percent() {
        _uiState.value.userInput + "%"
        _uiState.value.calculation + " / 100"
        resultReady()
    }

    //
    private fun calculate() {
        if (_uiState.value.result.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(
                userInput = _uiState.value.result,
                calculation = _uiState.value.result,
                isCalculationComplete = true
            )
            resultReady()

            _uiState.value = _uiState.value.copy(
                result = ""
            )
        }
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