package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.presentation.block.BlockEvent


@Composable
fun BlockHeader(
    block: Block,
    onEvent: (BlockEvent) -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = block.name,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(4.dp))
        Divider(
            modifier = Modifier.width(64.dp),
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(onClick = {
                onEvent(BlockEvent.AddSession)
            }) {
                Text(text = stringResource(id = R.string.new_session))
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = {
                    onEvent(BlockEvent.EndBlock)
                },
                enabled = !block.completed
            ) {
                Text(text = stringResource(id = R.string.end_block))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    BlockHeader(
        block = Block(name = "Block 1"),
        onEvent = {}
    )
}