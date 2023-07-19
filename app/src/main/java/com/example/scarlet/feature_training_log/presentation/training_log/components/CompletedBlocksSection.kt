package com.example.scarlet.feature_training_log.presentation.training_log.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
fun CompletedBlocksSection(
    navigator: DestinationsNavigator,
    completedBlocks: List<BlockWithSessions> = emptyList(),
    onEvent: (TrainingLogEvent) -> Unit
) {
    TitledLazyList(
        modifier = Modifier
            .fillMaxWidth()
            .padding(TitleLazyListPadding),
        title = stringResource(R.string.completed_training_blocks)
    ) {
        if (completedBlocks.isNotEmpty()) {
            items(completedBlocks) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = MainButtonContentPadding,
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    onClick = {
                        navigator.navigate(BlockScreenDestination(it.block))
                    }
                ) {
                    DeletableItem (
                        modifier = Modifier.fillMaxSize(),
                        onDeleteClicked = {
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
                                text =
                                if (it.sessions.isNotEmpty()) {
                                    DateUtils.formatDate(it.sessions.first().date) + " - " +
                                            DateUtils.formatDate(it.sessions.last().date)
                                } else {
                                    stringResource(R.string.empty_block)
                                },
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }

                }
            }
        } else {
            item {
                Text(
                    text = stringResource(R.string.no_completed_training_blocks),
                    style = MaterialTheme.typography.bodyMedium
                    // TODO color = grey
                )
            }
        }
    }
}