package com.example.scarlet.feature_training_log.presentation.training_log.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.training_log.TrainingLogEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBlockSheet(
    onEvent: (TrainingLogEvent) -> Unit
) {
    var blockName by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.new_block),
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = blockName,
                onValueChange = { blockName = it },
                placeholder = {
                    Text(
                        text = stringResource(R.string.block_name),
                        /* TODO set color */
                    )
                },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.align(Alignment.End)
            ) {
                OutlinedButton(onClick = {
                    onEvent(TrainingLogEvent.HideNewBlockSheet)
                }) {
                    Text(stringResource(R.string.cancel))
                }

                Spacer(modifier = Modifier.width(4.dp))

                Button(
                    onClick = {
                        onEvent(TrainingLogEvent.CreateBlock(blockName))
                    },
                    enabled = blockName.isNotBlank()
                ) {
                    Text(stringResource(R.string.create_block))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}