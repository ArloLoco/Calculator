package com.example.calculator.ui

object ExpressionEvaluator {

    private fun insertMultiply(expression: String, index: Int): String {
        return expression.substring(0, index) + " * " + expression.substring(index)
    }

    private fun implicitMultiplication(expression: String):String {

        var processedExpression: String = expression
        val indexes = mutableListOf<Int>()

        for ((index, char) in expression.withIndex()) {
            if (index > 0 && char == '(' && expression[index - 1].isDigit()) {
                indexes += index - 1
            } else if ( char == ')' && index < expression.length && expression[index + 1].isDigit()) {
                indexes += index + 1
            }
        }

        for (position in indexes.sortedDescending()) {
            processedExpression = insertMultiply(processedExpression, position)
        }

        return processedExpression
    }

    private fun shuntingYard(expression: String): List<String> {

        val outputQueue: MutableList<String> = mutableListOf()
        val operatorStack: MutableList<String> = mutableListOf()

        // create an immutable map of key value pairs for precedence
        val operatorPrecedence = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2)
        // lambda expression taking token of type String returning boolean value if token is an operator
        val isOperator = { token: String -> token in operatorPrecedence.keys }

        // pre-processing for brackets, and splits expression into component parts
        // TODO implicit multiplication for brackets
        val tokens = expression
            .replace("(", " ( ")
            .replace(")", " ) ")
            .split("\\s+".toRegex())
            .filter { it.isNotBlank() }

        // iterates through tokens placing them in the correct stack
        for (token in tokens) {
            when {
                // is token a number
                token.toDoubleOrNull() != null -> outputQueue.add(token)
                // checks if operator and adds to stack or queue
                isOperator(token) -> {
                    while (operatorStack.isNotEmpty() && isOperator(operatorStack.last()) &&
                        operatorPrecedence[token]!! <= operatorPrecedence[operatorStack.last()]!!
                    ) {
                        outputQueue.add(operatorStack.removeAt(operatorStack.size - 1))
                    }
                    // if operator stack empty
                    operatorStack.add(token)
                }
                // TODO handle multiple brackets???
                // open bracket processing
                token == "(" -> operatorStack.add(token)
                //closed bracket processing
                token == ")" -> {
                    while (operatorStack.isNotEmpty() && operatorStack.last() != "(") {
                        outputQueue.add(operatorStack.removeAt(operatorStack.size - 1))
                    }
                    if (operatorStack.isNotEmpty() && operatorStack.last() == "(") {
                        operatorStack.removeAt(operatorStack.size - 1) // Pop the '('
                    }
                }
            }
        }
        // clears remaining operator stack
        while (operatorStack.isNotEmpty()) {
            outputQueue.add(operatorStack.removeAt(operatorStack.size - 1))
        }

        return outputQueue
    }

    private fun evaluatePostfix(postfixTokens: List<String>): Double {
        val stack: MutableList<Double> = mutableListOf()

        for (token in postfixTokens) {
            when {
                // add number to stack
                token.toDoubleOrNull() != null -> stack.add(token.toDouble())
                // deals with operator
                token in listOf("+", "-", "*", "/") -> {
                    val right = stack.removeAt(stack.size - 1)
                    val left = stack.removeAt(stack.size - 1)
                    val result = when (token) {
                        "+" -> left + right
                        "-" -> left - right
                        "*" -> left * right
                        "/" -> left / right
                        else -> throw IllegalArgumentException("Unknown operator.")
                    }
                    stack.add(result)
                }
            }
        }

        return stack.last()
    }

    private fun formatDoubleToString(evaluated: Double): String {
        val stringValue = evaluated.toString()

        return if (stringValue.endsWith(".0")) {
            stringValue.dropLast(2)
        } else {
            stringValue
        }
    }

    fun evaluateExpression(expression: String): String {
        val preProcessed = implicitMultiplication(expression)
        val postfix = shuntingYard(preProcessed)
        val evaluated = evaluatePostfix(postfix)

        return formatDoubleToString(evaluated)
    }
}