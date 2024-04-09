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

        updateState(
        userInput = _uiState.value.userInput + number,
        calculation = _uiState.value.calculation + number
        )

        resultReady()
    }

    // DONE
    private fun enterOperation(symbol: String, operator: String) {
        if (_uiState.value.userInput.isNotEmpty()) {
            val last = _uiState.value.calculation.takeLast(2)

            when {
                last == "+ " || last == "* " || last == "/ " || last == "- " -> {
                    delete()
                    updateState(
                        userInput = _uiState.value.userInput + symbol,
                        calculation = _uiState.value.calculation + operator
                    )
                }
                last == "(" || last == ")" -> if (symbol == "-") {
                    updateState(
                        userInput = _uiState.value.userInput + symbol,
                        calculation = _uiState.value.calculation + symbol
                    )
                }
                last.toDoubleOrNull() != null -> {
                    updateState(
                        userInput = _uiState.value.userInput + symbol,
                        calculation = _uiState.value.calculation + operator,
                        isCalculationComplete = false
                    )
                }
                else -> { }
            }
        } else if (symbol == "-") {
            updateState(
                userInput = _uiState.value.userInput + symbol,
                calculation = _uiState.value.calculation + symbol
            )
        }
        resultReady()
    }

    // DONE
    private fun delete() {
        if (_uiState.value.userInput.isNotEmpty()) {
            updateState(
                userInput = _uiState.value.userInput.dropLast(1)
            )


            if (!_uiState.value.calculation.endsWith(' ')) {
                updateState(
                    calculation = _uiState.value.calculation.dropLast(1)
                )
            } else {
                updateState(
                    calculation = _uiState.value.calculation.dropLast(3)
                )
            }

            resultReady()
        }
    }

    // DONE
    private fun allClear() {
        updateState(
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
            updateState(
                userInput = "0.",
                calculation = "0."
            )
        } else {
            updateState(
                userInput = _uiState.value.userInput + ".",
                calculation = _uiState.value.calculation + "."
            )
        }
        resultReady()
    }

    // DONE
    private fun enterParentheses() {
        val countOpen = _uiState.value.userInput.count { it == '(' }
        val countClose = _uiState.value.userInput.count { it == ')' }
        val total = countOpen - countClose

        val openOrClose = if (total <= 0 || _uiState.value.userInput.endsWith('(')) '(' else ')'

        updateState(
            isCalculationComplete = false
        )
        updateState(
            userInput = _uiState.value.userInput + openOrClose,
            calculation = _uiState.value.calculation + openOrClose
        )
        resultReady()
    }

    // DONE
    private fun percent() {
        updateState(
            userInput = _uiState.value.userInput + "%",
            calculation = _uiState.value.calculation + " / 100"
        )
        resultReady()
    }

    //
    private fun calculate() {
        if (_uiState.value.result.isNotEmpty()) {
            updateState(
                userInput = _uiState.value.result,
                calculation = _uiState.value.result,
                isCalculationComplete = true
            )
            resultReady()

            updateState(
                result = ""
            )
        }
    }

    //
    private fun resultReady() {
        updateState(
            result = ""
        )
        val currentCalculation = _uiState.value.calculation

        if ((currentCalculation.contains(' ') || currentCalculation.contains('('))
                    &&
                    (currentCalculation.last().toString().toDoubleOrNull() != null || currentCalculation.last() == '.')) {
            updateState(
                result = ExpressionEvaluator.evaluateExpression(currentCalculation)
            )
        }
    }

    private fun updateState(
        userInput: String? = null,
        calculation: String? = null,
        result: String? = null,
        isCalculationComplete: Boolean? = null
    ) {
        _uiState.value = _uiState.value.copy(
            userInput = userInput ?: _uiState.value.userInput,
            calculation = calculation ?: _uiState.value.calculation,
            result = result ?: _uiState.value.result,
            isCalculationComplete = isCalculationComplete ?: _uiState.value.isCalculationComplete
        )
    }
}