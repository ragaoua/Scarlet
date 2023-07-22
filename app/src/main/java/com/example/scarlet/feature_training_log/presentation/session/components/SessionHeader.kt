package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.core.DateUtils
import com.example.scarlet.feature_training_log.presentation.session.SessionEvent
import com.example.scarlet.feature_training_log.presentation.session.SessionUiState
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionHeader(
    state: SessionUiState,
    onEvent: (SessionEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(state.isDatePickerDialogOpen) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = state.session.date.time
            )
            DatePickerDialog(
                onDismissRequest = { onEvent(SessionEvent.ToggleDatePickerDialog) },
                confirmButton = {
                    Button(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { dateMillis ->
                                onEvent(SessionEvent.UpdateSessionDate(Date(dateMillis)))
                            }

                        },
                        enabled = datePickerState.selectedDateMillis != null
                    ) {
                        Text(text = stringResource(R.string.save))
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { onEvent(SessionEvent.ToggleDatePickerDialog) }
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
        Text(
            text = state.sessionBlockName,
            style = MaterialTheme.typography.headlineSmall,
            // TODO : color = grey
        )
        Spacer(modifier = Modifier.height(4.dp))
        Divider(
            modifier = Modifier.width(64.dp),
            thickness = 1.dp
        )
        Spacer(modifier = Modifier
            .height(8.dp)
            .background(Color.Black))

        Box {
            val editIconPadding = 24.dp

            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = stringResource(R.string.edit_session_date),
                modifier = Modifier
                    .size(editIconPadding)
                    .align(Alignment.CenterEnd)
                    .clickable { onEvent(SessionEvent.ToggleDatePickerDialog) }
            )
            Text(
                text = DateUtils.formatDate(state.session.date),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(horizontal = editIconPadding + 4.dp)
                    .align(Alignment.Center)
            )
        }
    }
}