package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.core.components.SecondaryActionButton
import com.example.scarlet.feature_training_log.presentation.session.SessionEvent
import com.example.scarlet.feature_training_log.presentation.session.SessionUiState

@Composable
fun MovementSelectionSheet(
    state: SessionUiState,
    onEvent: (SessionEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.select_movement),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Divider(
            modifier = Modifier.width(96.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        SecondaryActionButton(
            onClick = {
                // TODO
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_new_movement)
            )
            Text(
                text = stringResource(R.string.add_new_movement_btn_msg),
                style = MaterialTheme.typography.titleLarge // TODO : check if OK
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(state.movements){ movement ->
                Text(
                    text = movement.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .clickable {
                            onEvent(SessionEvent.NewExercise(movement.id))
                        }
                        .border(1.dp, MaterialTheme.colorScheme.onSurfaceVariant)
                        .padding(8.dp)
                )
            }
        }
    }
}