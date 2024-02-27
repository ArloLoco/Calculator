package com.example.calculator.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.calculator.R
import com.example.calculator.ui.CalculatorAction
import com.example.calculator.ui.CalculatorOperation

sealed class ButtonContent {
    data class Text(
        @StringRes val resourceId: Int,
        val backgroundColor: @Composable () -> Color = { MaterialTheme.colorScheme.primaryContainer }
    ) : ButtonContent()
    data class Drawable(
        @DrawableRes val resourceId: Int,
        @StringRes val contentDescription: Int,
        val backgroundColor: @Composable () -> Color = { MaterialTheme.colorScheme.primaryContainer }
    ) : ButtonContent()
}

val buttonActionsMap = mapOf(
    R.string.zero to CalculatorAction.Number("0"),
R.string.one to CalculatorAction.Number("1"),
R.string.two to CalculatorAction.Number("2"),
R.string.three to CalculatorAction.Number("3"),
R.string.four to CalculatorAction.Number("4"),
R.string.five to CalculatorAction.Number("5"),
R.string.six to CalculatorAction.Number("6"),
R.string.seven to CalculatorAction.Number("7"),
R.string.eight to CalculatorAction.Number("8"),
R.string.nine to CalculatorAction.Number("9"),
R.string.add to CalculatorAction.Operation(CalculatorOperation.Add),
R.string.minus to CalculatorAction.Operation(CalculatorOperation.Minus),
R.string.multiply to CalculatorAction.Operation(CalculatorOperation.Multiply),
R.string.divide to CalculatorAction.Operation(CalculatorOperation.Divide),
R.string.all_clear to CalculatorAction.AllClear,
R.string.parenthesis to CalculatorAction.Parentheses,
R.string.percent to CalculatorAction.Percent,
R.string.decimal_point to CalculatorAction.Decimal,
R.string.equals to CalculatorAction.Calculate
)

object ButtonData {
    val row1Buttons = listOf(
        ButtonContent.Text(R.string.all_clear, { MaterialTheme.colorScheme.secondary }),
        ButtonContent.Text(R.string.parenthesis, { MaterialTheme.colorScheme.tertiary }),
        ButtonContent.Text(R.string.percent, { MaterialTheme.colorScheme.tertiary }),
        ButtonContent.Text(R.string.divide, { MaterialTheme.colorScheme.tertiary })
    )
    val row2Buttons = listOf(
        ButtonContent.Text(R.string.seven),
        ButtonContent.Text(R.string.eight),
        ButtonContent.Text(R.string.nine),
        ButtonContent.Text(R.string.multiply, { MaterialTheme.colorScheme.tertiary })
    )
    val row3Buttons = listOf(
        ButtonContent.Text(R.string.four),
        ButtonContent.Text(R.string.five),
        ButtonContent.Text(R.string.six),
        ButtonContent.Text(R.string.minus, { MaterialTheme.colorScheme.tertiary })
    )
    val row4Buttons = listOf(
        ButtonContent.Text(R.string.one),
        ButtonContent.Text(R.string.two),
        ButtonContent.Text(R.string.three),
        ButtonContent.Text(R.string.add, { MaterialTheme.colorScheme.tertiary })
    )
    val row5Buttons = listOf(
        ButtonContent.Text(R.string.zero),
        ButtonContent.Text(R.string.decimal_point),
        ButtonContent.Drawable(R.drawable.backspace, R.string.cd_backspace),
        ButtonContent.Text(R.string.equals, { MaterialTheme.colorScheme.inversePrimary })
    )
}