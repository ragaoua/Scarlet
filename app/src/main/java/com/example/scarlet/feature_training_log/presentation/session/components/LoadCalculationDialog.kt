package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.example.scarlet.feature_training_log.presentation.session.SessionEvent
import com.example.scarlet.feature_training_log.presentation.session.SessionUiState

@Composable
fun LoadCalculationDialog(
    dialogState: SessionUiState.LoadCalculationDialogState,
    onEvent: (SessionEvent) -> Unit
) {
    // TODO test if the state.loadCalculationDialogState.set is not null ??
    var percentage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = {
            onEvent(SessionEvent.HideLoadCalculationDialog)
        },
        confirmButton = {
            Button(onClick = {
                onEvent(SessionEvent.CalculateLoad(percentage))
            }) {
                Text("Confirm")
            }

        },
        title = {
            Text(
                text = "Update load based on the previous set",
                textAlign = TextAlign.Center
            )
        },
        text = {
            val focusRequester = remember { FocusRequester() }
            SideEffect { focusRequester.requestFocus() }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = percentage,
                    onValueChange = { percentage = it },
                    trailingIcon = { Text(text = "%") },
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Right
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onEvent(SessionEvent.CalculateLoad(percentage)) }
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .weight(1f)
                    // TODO : limit to < 100
                )
                Text(
                    text = " x ${dialogState.previousSet.weight} kg" // TODO kg or lbs ???
                )
            }

        }
    )
}