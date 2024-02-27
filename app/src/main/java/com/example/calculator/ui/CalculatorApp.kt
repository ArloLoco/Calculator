package com.example.calculator.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculator.R
import com.example.calculator.data.ButtonContent
import com.example.calculator.data.ButtonData
import com.example.calculator.data.buttonActionsMap
import com.example.calculator.ui.theme.CalculatorTheme

@Composable
fun CalculatorApp() {
    CalculatorButtonsLayout()
}

@Composable
fun CalculatorButtonsLayout(
    calcViewModel: CalculatorViewModel = viewModel()
){
    val buttonRows = listOf(
        ButtonData.row1Buttons,
        ButtonData.row2Buttons,
        ButtonData.row3Buttons,
        ButtonData.row4Buttons,
        ButtonData.row5Buttons
    )

    val maxWidth = LocalConfiguration.current.screenWidthDp.dp
    val paddingBetweenButtons = dimensionResource(R.dimen.padding_medium)
    val totalPadding = paddingBetweenButtons * (4 - 1)
    val buttonSize = (maxWidth - totalPadding) / 4

    val calcUiState by calcViewModel.uiState.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .navigationBarsPadding()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Spacer(
               modifier = Modifier
                   .height(24.dp)
            )
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = stringResource(R.string.settings),
                    )
                }
            }
            Text(
                text = calcUiState.userInput,
                textAlign = TextAlign.Right,
                fontSize = 64.sp,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = dimensionResource(R.dimen.padding_small),
                        end = dimensionResource(R.dimen.padding_small)
                    )
            )
            Text(
                text = calcUiState.result,
                textAlign = TextAlign.Right,
                fontSize = 32.sp,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small))
            )
            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
                .weight(2.5f)
        ) {

            buttonRows.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = paddingBetweenButtons / 2,
                            vertical = paddingBetweenButtons / 4
                        )
                        .weight(1f)
                        .aspectRatio(1f)
                ) {
                    row.forEach { buttonContent ->
                        val action = when (buttonContent) {
                            is ButtonContent.Text -> buttonActionsMap[buttonContent.resourceId]
                            is ButtonContent.Drawable -> buttonActionsMap[buttonContent.contentDescription]
                            }
                        CalculatorButton(
                            content = buttonContent,
                            buttonSize = buttonSize,
                            onClick = { action?.let(calcViewModel::onAction)}
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    content: ButtonContent,
    buttonSize: Dp,
    onClick : () -> Unit
) {
    val backgroundColor = when (content) {
        is ButtonContent.Text -> content.backgroundColor()
        is ButtonContent.Drawable -> content.backgroundColor()
    }

    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor),
        modifier = Modifier
            .size(buttonSize)
    ) {
        when (content) {
            is ButtonContent.Text -> Text(
                text = stringResource(content.resourceId),
                fontSize = 32.sp
            )
            is ButtonContent.Drawable -> Icon(
                painter = painterResource(content.resourceId),
                contentDescription = stringResource(content.contentDescription),
                modifier = Modifier
                    .fillMaxSize(0.8f)
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun CalculatorPreview() {
    CalculatorTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            CalculatorApp()
        }
    }
}