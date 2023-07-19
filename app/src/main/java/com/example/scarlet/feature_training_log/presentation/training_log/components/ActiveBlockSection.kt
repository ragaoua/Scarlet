package com.example.scarlet.feature_training_log.presentation.training_log.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.BlockWithSessions
import com.example.scarlet.feature_training_log.presentation.core.components.DeletableItem
import com.example.scarlet.feature_training_log.presentation.core.components.TitledLazyList
import com.example.scarlet.feature_training_log.presentation.core.DateUtils
import com.example.scarlet.feature_training_log.presentation.destinations.BlockScreenDestination
import com.example.scarlet.feature_training_log.presentation.training_log.TrainingLogEvent
import com.example.scarlet.ui.theme.MainButtonContentPadding
import com.example.scarlet.ui.theme.TitleLazyListPadding
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun ActiveBlockSection(
    navigator: DestinationsNavigator,
    activeBlockWithSessions: BlockWithSessions? = null,
    onEvent: (TrainingLogEvent) -> Unit
) {
    TitledLazyList(
        modifier = Modifier
            .fillMaxWidth()
            .padding(TitleLazyListPadding),
        title = stringResource(R.string.active_training_block),
    ) {
        item(activeBlockWithSessions) {
            activeBlockWithSessions?.let { activeBlockWithSessions ->
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = MainButtonContentPadding,
                    shape = MaterialTheme.shapes.large,
                    onClick = {
                        navigator.navigate(BlockScreenDestination(activeBlockWithSessions.block))
                    }
                ) {
                    DeletableItem (
                        modifier = Modifier.fillMaxSize(),
                        onDeleteClicked = {
                            onEvent(TrainingLogEvent.DeleteBlock(activeBlockWithSessions.block))
                        }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = activeBlockWithSessions.block.name,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text =
                                if (activeBlockWithSessions.sessions.isNotEmpty()) {
                                    stringResource(
                                        id = R.string.block_started_on,
                                        DateUtils.formatDate(activeBlockWithSessions.sessions.first().date)
                                    )
                                } else {
                                    stringResource(R.string.empty_block)
                                },
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            } ?: run {
                OutlinedButton(
                    contentPadding = MainButtonContentPadding,
                    shape = MaterialTheme.shapes.large,
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 4.dp
                    ),
                    onClick = {
                        onEvent(TrainingLogEvent.ShowNewBlockSheet)
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