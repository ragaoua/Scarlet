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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.core.components.SecondaryActionButton
import com.example.scarlet.feature_training_log.presentation.session.SessionEvent
import com.example.scarlet.feature_training_log.presentation.session.SessionUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementSelectionSheet(
    state: SessionUiState,
    onEvent: (SessionEvent) -> Unit
) {
    var movementNameFilter by remember { mutableStateOf("")}

    ModalBottomSheet(
        onDismissRequest = {
            onEvent(SessionEvent.HideMovementSelectionSheet)
        },
        sheetState = rememberModalBottomSheetState(
             skipPartiallyExpanded = true
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp),
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

            Spacer(modifier = Modifier.height(32.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(state.movements.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.no_movements_found),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if(movementNameFilter.isBlank()) {
                            Text(
                                text = stringResource(R.string.enter_movement_name),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            SecondaryActionButton(
                                onClick = {
                                    onEvent(SessionEvent.AddMovement(movementNameFilter))
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(R.string.add_new_movement)
                                )
                                Text(
                                    text = stringResource(
                                        R.string.add_new_movement_btn_msg,
                                        movementNameFilter
                                    ),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }
                } else {
                    items(state.movements) { movement ->
                        Text(
                            text = movement.name,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp)
                                .clickable {
                                    onEvent(SessionEvent.SelectMovement(movement.id))
                                }
                                .border(1.dp, MaterialTheme.colorScheme.onSurfaceVariant)
                                .padding(8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = movementNameFilter,
                onValueChange = {
                    movementNameFilter = it
                    onEvent(SessionEvent.FilterMovementsByName(movementNameFilter))
                },
                placeholder = {
                    Text(stringResource(R.string.enter_movement))
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}