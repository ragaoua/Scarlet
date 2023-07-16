package com.example.scarlet.feature_training_log.presentation.training_log.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.training_log.TrainingLogEvent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewBlockSheet(
    onEvent: (TrainingLogEvent) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.new_block),
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(16.dp))

            var blockName by remember { mutableStateOf("") }
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

            Button(
                onClick = {
                    onEvent(TrainingLogEvent.CreateBlock(blockName))
                },
                modifier = Modifier.align(Alignment.End),
                enabled = blockName.isNotBlank()
            ) {
                Text(stringResource(R.string.create_block))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}