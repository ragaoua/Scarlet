package com.example.scarlet.feature_training_log.presentation.training_log.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.BlockWithDates
import com.example.scarlet.feature_training_log.presentation.components.ScarletList
import com.example.scarlet.feature_training_log.presentation.destinations.BlockScreenDestination
import com.example.scarlet.feature_training_log.presentation.training_log.TrainingLogEvent
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Composable
fun ActiveBlockSection(
    navigator: DestinationsNavigator,
    activeBlock: BlockWithDates? = null,
    onEvent: (TrainingLogEvent) -> Unit
) {
    activeBlock?.let { block ->
        ScarletList(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.active_training_block),
            items = listOf(block),
            onItemClicked = {
                navigator.navigate(BlockScreenDestination(it.block))
            },
            onDeleteClicked = {
                onEvent(TrainingLogEvent.DeleteBlock(it.block))
            },
            itemColors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Column {
                Text(
                    text = it.block.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Started on ${it.firstSessionDate}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    } ?: run {
        ScarletList(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.active_training_block),
            items = listOf(null),
            onItemClicked = {
                onEvent(TrainingLogEvent.ShowNewBlockDialog)
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add new block" /* TODO : create resource */
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = stringResource(R.string.no_active_block_msg),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(R.string.start_new_block_msg),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoBlockPreview() {
    ActiveBlockSection(
        navigator = EmptyDestinationsNavigator,
        activeBlock = null,
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ActiveBlockPreview() {
    ActiveBlockSection(
        navigator = EmptyDestinationsNavigator,
        activeBlock = BlockWithDates(
            block = Block(name = "Block 1"),
            firstSessionDate = "01/01/2021",
            lastSessionDate = "01/02/2021"
        ),
        onEvent = {}
    )
}