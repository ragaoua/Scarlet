package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.session.SessionEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMovementSheet(
    onEvent: (SessionEvent) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
            onEvent(SessionEvent.ToggleNewMovementSheet)
        }
    ) {
        val focusRequester = remember { FocusRequester() }
        SideEffect {
            focusRequester.requestFocus()
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.new_movement),
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Divider(
                    modifier = Modifier.width(64.dp),
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(32.dp))

                var movementName by remember { mutableStateOf("") }
                OutlinedTextField(
                    modifier = Modifier.focusRequester(focusRequester),
                    value = movementName,
                    onValueChange = { movementName = it },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.movement_name),
                            /* TODO set color */
                        )
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        onEvent(SessionEvent.AddMovement(movementName))
                    },
                    modifier = Modifier.align(Alignment.End),
                    enabled = movementName.isNotBlank()
                ) {
                    Text(stringResource(R.string.create_movement))
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}