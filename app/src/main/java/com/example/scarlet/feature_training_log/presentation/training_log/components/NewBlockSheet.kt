package com.example.scarlet.feature_training_log.presentation.training_log.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.training_log.TrainingLogEvent
import com.example.scarlet.feature_training_log.presentation.training_log.TrainingLogUiState

/**
 * Bottom sheet that allows the user to create a new block.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBlockSheet(
    state: TrainingLogUiState,
    onEvent: (TrainingLogEvent) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onEvent(TrainingLogEvent.ToggleNewBlockSheet) }
    ) {
        // Used to automatically focus the text field when the sheet is shown
        val focusRequester = remember { FocusRequester() }
        SideEffect { focusRequester.requestFocus() }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /*************************************************************************
                 * Sheet title
                 *************************************************************************/
                Text(
                    text = stringResource(R.string.new_block),
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(modifier = Modifier.width(64.dp))

                Spacer(modifier = Modifier.height(32.dp))

                /*************************************************************************
                 * Block name text field
                 *************************************************************************/
                OutlinedTextField(
                    modifier = Modifier.focusRequester(focusRequester),
                    value = state.newBlockName,
                    onValueChange = { onEvent(TrainingLogEvent.UpdateNewBlockName(it)) },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.block_name),
                            /* TODO set color */
                        )
                    },
                    isError = state.newBlockSheetTextFieldError != null,
                    supportingText = state.newBlockSheetTextFieldError?.let { error ->
                        { Text(stringResource(error.resId, *error.args)) }
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))


                /*************************************************************************
                 * Validation button
                 *************************************************************************/
                Button(
                    onClick = {
                        onEvent(TrainingLogEvent.AddBlock(state.newBlockName))
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(stringResource(R.string.create_block))
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}