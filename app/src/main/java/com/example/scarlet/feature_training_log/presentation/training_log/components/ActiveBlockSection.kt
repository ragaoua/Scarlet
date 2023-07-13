package com.example.scarlet.feature_training_log.presentation.training_log.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.scarlet.feature_training_log.presentation.components.TitledLazyList
import com.example.scarlet.feature_training_log.presentation.components.ScarletListItem
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
    TitledLazyList(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
        title = stringResource(R.string.active_training_block),
    ) {
        item(activeBlock) {
            ScarletListItem(
                onClick = {
                    activeBlock?.let {
                        navigator.navigate(BlockScreenDestination(it.block))
                    } ?: onEvent(TrainingLogEvent.ShowNewBlockDialog)
                },
                onDelete = {
                    activeBlock?.let {
                        onEvent(TrainingLogEvent.DeleteBlock(it.block))
                    }
                }
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val headline = activeBlock?.block?.name ?: stringResource(R.string.no_active_block)
                    val subhead = activeBlock?.let {
                        "Started on ${activeBlock.firstSessionDate}"
                    } ?: stringResource(R.string.start_new_block)

                    Text(
                        text = headline,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = subhead,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
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