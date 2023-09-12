package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.scarlet.feature_training_log.presentation.block.BlockEvent
import com.example.scarlet.feature_training_log.presentation.block.BlockUiState

/**
 * Bottom sheet that allows the user to edit a block.
 *
 * @param sheetState the state of the sheet
 * @param onEvent the event handler
 *
 * @see BlockUiState.EditBlockSheetState
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBlockSheet(
    sheetState: BlockUiState.EditBlockSheetState,
    onEvent: (BlockEvent) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onEvent(BlockEvent.CancelBlockEdition) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /*************************************************************************
             * Sheet title
             *************************************************************************/
            Text(
                text = stringResource(R.string.edit_block),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(modifier = Modifier.width(64.dp))

            Spacer(modifier = Modifier.height(32.dp))

            /*************************************************************************
             * Block name text field
             *************************************************************************/
            val focusRequester = remember { FocusRequester() }
            SideEffect {
                focusRequester.requestFocus()
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = sheetState.blockName,
                onValueChange = { onEvent(BlockEvent.UpdateEditedBlockName(it)) },
                label = {
                    Text(stringResource(R.string.block_name))
                    // TODO set the color to a lighter one
                },
                isError = sheetState.blockNameError != null,
                supportingText = sheetState.blockNameError?.let { error ->
                    { Text(stringResource(error.resId, *error.args)) }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            /*************************************************************************
             * Validation button
             *************************************************************************/
            Button(
                onClick = { onEvent(BlockEvent.SaveBlockName) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(stringResource(R.string.done))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}