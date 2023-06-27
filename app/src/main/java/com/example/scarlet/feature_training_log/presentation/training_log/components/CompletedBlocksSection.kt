package com.example.scarlet.feature_training_log.presentation.training_log.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.presentation.components.ScarletList
import com.example.scarlet.feature_training_log.presentation.components.ScarletListTitle
import com.example.scarlet.feature_training_log.presentation.destinations.BlockScreenDestination
import com.example.scarlet.feature_training_log.presentation.training_log.TrainingLogEvent
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Composable
fun CompletedBlocksSection(
    navigator: DestinationsNavigator,
    completedBlocks: List<Block> = emptyList(),
    onEvent: (TrainingLogEvent) -> Unit
) {
    if (completedBlocks.isNotEmpty()) {
        ScarletList(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.completed_training_blocks),
            items = completedBlocks,
            onItemClicked = { block ->
                navigator.navigate(BlockScreenDestination(block))
            },
            onDeleteClicked = { block ->
                onEvent(TrainingLogEvent.DeleteBlock(block))
            }
        ) { block ->
            Column {
                Text(
                    text = block.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "XX/XX/XXXX - XX/XX/XXXX", /* TODO*/
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            ScarletListTitle(title = stringResource(R.string.completed_training_blocks))
            Text(
                text = stringResource(R.string.no_completed_training_blocks),
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun NoBlocksPreview() {
    CompletedBlocksSection(
        navigator = EmptyDestinationsNavigator,
        completedBlocks = emptyList(),
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun CompletedBlocksPreview() {
    CompletedBlocksSection(
        navigator = EmptyDestinationsNavigator,
        completedBlocks = listOf(
            Block(name = "Block 1"),
            Block(name = "Block 2"),
            Block(name = "Block 3")
        ),
        onEvent = {}
    )
}