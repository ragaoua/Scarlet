package com.example.scarlet.feature_training_log.presentation.training_log.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.training_log.TrainingLogEvent
import com.example.scarlet.feature_training_log.presentation.training_log.TrainingLogUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBlockDialog(
    state: TrainingLogUiState,
    onEvent: (TrainingLogEvent) -> Unit
) {
    var blockName by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = {
            onEvent(TrainingLogEvent.ShowNewBlockDialog)
        },
        title = {
            Text(stringResource(R.string.new_block))
        },
        text  = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if(state.isShowingBlockNameEmptyMsg) {
                    Text(stringResource(R.string.block_name_empty))
                }
                TextField(
                    value = blockName,
                    onValueChange = { blockName = it }
                )
            }

        },
        confirmButton = {
            Button(onClick = {
                onEvent(TrainingLogEvent.CreateBlock(blockName))
            }) {
                Text(stringResource(R.string.create_block))
            }
        },
        dismissButton = {
            Button(onClick = {
                onEvent(TrainingLogEvent.HideNewBlockDialog)
            }) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Preview
@Composable
fun DialogPreview() {
    NewBlockDialog(
        state = TrainingLogUiState(
            isShowingBlockNameEmptyMsg = false
        ),
        onEvent = {}
    )
}

@Preview
@Composable
fun ErrorMessagePreview() {
    NewBlockDialog(
        state = TrainingLogUiState(
            isShowingBlockNameEmptyMsg = true
        ),
        onEvent = {}
    )
}