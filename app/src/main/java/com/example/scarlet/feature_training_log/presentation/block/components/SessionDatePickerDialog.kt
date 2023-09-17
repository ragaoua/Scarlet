package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.presentation.block.BlockEvent
import java.util.Date

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SessionDatePickerDialog(
    session: Session,
    onEvent: (BlockEvent) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = session.date.time
    )
    DatePickerDialog(
        onDismissRequest = { onEvent(BlockEvent.HideSessionDatePickerDialog) },
        confirmButton = {
            Button(
                onClick = {
                    datePickerState.selectedDateMillis?.let { dateMillis ->
                        onEvent(BlockEvent.UpdateSessionDate(Date(dateMillis)))
                    }
                },
                enabled = datePickerState.selectedDateMillis != null
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = { onEvent(BlockEvent.HideSessionDatePickerDialog) }
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}