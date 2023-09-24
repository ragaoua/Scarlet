package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.block.BlockEvent
import com.example.scarlet.feature_training_log.presentation.block.BlockUiState

/**
 * Bottom sheet that allows the user to edit or delete a movement.
 *
 * @param sheetState the state of the sheet
 * @param onEvent the event handler
 *
 * @see BlockUiState.EditMovementSheetState
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMovementSheet(
    sheetState: BlockUiState.EditMovementSheetState,
    onEvent: (BlockEvent) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onEvent(BlockEvent.HideEditMovementSheet) }
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
                text = stringResource(R.string.edit_movement),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(modifier = Modifier.width(64.dp))

            Spacer(modifier = Modifier.height(32.dp))

            /*************************************************************************
             * Movement name text field
             *************************************************************************/
            val focusRequester = remember { FocusRequester() }
            SideEffect { focusRequester.requestFocus() }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = sheetState.editedMovementName,
                onValueChange = { onEvent(BlockEvent.UpdateEditedMovementName(it)) },
                label = {
                    Text(stringResource(R.string.movement_name))
                    // TODO set the color to a lighter one
                },
                isError = sheetState.movementNameError != null,
                supportingText = sheetState.movementNameError?.let { error ->
                    { Text(stringResource(error.resId, *error.args)) }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onEvent(BlockEvent.UpdateEditedMovement) }
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            /*************************************************************************
             * Buttons
             *************************************************************************/
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { onEvent(BlockEvent.DeleteEditedMovement) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(R.string.delete_movement))
                    /* TODO : show a dialog indicating if exercises use this movement */
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { onEvent(BlockEvent.UpdateEditedMovement) },
                ) {
                    Text(stringResource(R.string.save))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}