package com.example.scarlet.feature_training_log.presentation.training_log.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.BlockWithDates
import com.example.scarlet.feature_training_log.presentation.components.TitledLazyList
import com.example.scarlet.feature_training_log.presentation.destinations.BlockScreenDestination
import com.example.scarlet.feature_training_log.presentation.training_log.TrainingLogEvent
import com.example.scarlet.ui.theme.MainButtonContentPadding
import com.example.scarlet.ui.theme.TitleLazyListPadding
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
            .padding(TitleLazyListPadding),
        title = stringResource(R.string.active_training_block),
    ) {
        item(activeBlock) {
            activeBlock?.let {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = MainButtonContentPadding,
                    shape = MaterialTheme.shapes.large,
                    onClick = {
                        navigator.navigate(BlockScreenDestination(it.block))
                    }
//                onDelete = {
//                    onEvent(TrainingLogEvent.DeleteBlock(it.block))
//                } /* TODO */
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = it.block.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Started on ${it.firstSessionDate}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            } ?: run {
                OutlinedButton(
                    contentPadding = MainButtonContentPadding,
                    shape = MaterialTheme.shapes.large,
                    onClick = {
                        onEvent(TrainingLogEvent.ShowNewBlockDialog)
                    }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.no_active_block),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = stringResource(R.string.start_new_block),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
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