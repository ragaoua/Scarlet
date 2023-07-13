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
    ScarletList(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
        title = stringResource(R.string.active_training_block),
        items = listOf(activeBlock),
        onItemClicked = { block ->
            block?.let {
                navigator.navigate(BlockScreenDestination(it.block))
            } ?: onEvent(TrainingLogEvent.ShowNewBlockDialog)
        },
        onDeleteClicked = { block ->
            block?.let {
                onEvent(TrainingLogEvent.DeleteBlock(it.block))
            }
        }
    ) { it ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val headline = it?.block?.name ?: stringResource(R.string.no_active_block)
            val subhead = it?.let {
                "Started on ${it.firstSessionDate}"
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