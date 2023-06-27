package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            Button(onClick = {
                onEvent(BlockEvent.AddSession)
            }) {
                Text(text = stringResource(id = R.string.new_session))
            }
            /* TODO : check if the block is already completed */
            Button(onClick = {
                onEvent(BlockEvent.EndBlock)
            }) {
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