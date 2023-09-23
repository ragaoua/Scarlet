package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.block.BlockEvent
import com.example.scarlet.feature_training_log.presentation.block.BlockUiState
import com.example.scarlet.feature_training_log.presentation.core.bottomBorder
import com.example.scarlet.feature_training_log.presentation.core.components.NumberPicker

@Composable
fun LoadCalculationDialog(
    dialogState: BlockUiState.LoadCalculationDialogState,
    onEvent: (BlockEvent) -> Unit
) {
    val percentage by remember(dialogState.percentage) {
        mutableStateOf(dialogState.percentage?.toString() ?: "")
    }

    AlertDialog(
        onDismissRequest = {
            onEvent(BlockEvent.HideLoadCalculationDialog)
        },
        confirmButton = {
            Button(onClick = {
                onEvent(BlockEvent.UpdateSetBasedOnPrecedingSet)
            }) {
                Text(stringResource(R.string.confirm))
            }
        },
        title = {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.update_load),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(
                        R.string.base_load_kg,
                        dialogState.previousSet.weight.toString()
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            val focusRequester = remember { FocusRequester() }
            SideEffect { focusRequester.requestFocus() }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = percentage,
                    onValueChange = { onEvent(BlockEvent.UpdateLoadPercentage(it)) },
                    singleLine = true,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    textStyle = LocalTextStyle.current.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Right
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onEvent(BlockEvent.UpdateSetBasedOnPrecedingSet) }
                    ),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                innerTextField()
                            }
                            Text(
                                text = "%",
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    },
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .bottomBorder(
                            strokeWidth = 1.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        .width(96.dp)
                )

                Text(
                    text = "=",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                NumberPicker(
                    modifier = Modifier.weight(1f),
                    value = dialogState.calculatedLoad?.toString() ?: "",
                    onMinusClick = {
                        onEvent(BlockEvent.UpdateCalculatedLoad(it.toFloat() - 0.5f))
                    },
                    onPlusClick = {
                        onEvent(BlockEvent.UpdateCalculatedLoad(it.toFloat() + 0.5f))
                    },
                    isMinusButtonEnabled = (dialogState.calculatedLoad ?: 0f) > 0,
                    isPlusButtonEnabled = dialogState.calculatedLoad != null
                )
            }
        }
    )
}