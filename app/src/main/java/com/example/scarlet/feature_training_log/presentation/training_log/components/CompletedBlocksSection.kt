package com.example.scarlet.feature_training_log.presentation.training_log.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.BlockWithDates
import com.example.scarlet.feature_training_log.presentation.components.ScarletListItem
import com.example.scarlet.feature_training_log.presentation.components.TitledLazyList
import com.example.scarlet.feature_training_log.presentation.destinations.BlockScreenDestination
import com.example.scarlet.feature_training_log.presentation.training_log.TrainingLogEvent
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Composable
fun CompletedBlocksSection(
    navigator: DestinationsNavigator,
    completedBlocks: List<BlockWithDates> = emptyList(),
    onEvent: (TrainingLogEvent) -> Unit
) {
    TitledLazyList(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
        title = stringResource(R.string.completed_training_blocks)
    ) {
        if (completedBlocks.isNotEmpty()) {
            items(completedBlocks) {
                ScarletListItem(
                    onClick = {
                        navigator.navigate(BlockScreenDestination(it.block))
                    },
                    onDelete = {
                        onEvent(TrainingLogEvent.DeleteBlock(it.block))
                    }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = it.block.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "${it.firstSessionDate} - ${it.lastSessionDate}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        } else {
            item { /* TODO Define a key ???? */
                Text(
                    text = stringResource(R.string.no_completed_training_blocks),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
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
            BlockWithDates(
                block = Block(name = "Block 1"),
                firstSessionDate = "01/01/2021",
                lastSessionDate = "01/02/2021"
            ),
            BlockWithDates(
                block = Block(name = "Block 2"),
                firstSessionDate = "01/01/2021",
                lastSessionDate = "01/02/2021"
            )
        ),
        onEvent = {}
    )
}