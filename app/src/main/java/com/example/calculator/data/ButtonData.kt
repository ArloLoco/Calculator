package com.example.calculator.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.calculator.R

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