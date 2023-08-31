package com.example.scarlet.feature_training_log.presentation.training_log.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.BlockWithDays
import com.example.scarlet.feature_training_log.domain.model.DayWithSessions
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.presentation.core.DateUtils
import com.example.scarlet.feature_training_log.presentation.core.components.DeletableItem
import com.example.scarlet.feature_training_log.presentation.destinations.BlockScreenDestination
import com.example.scarlet.feature_training_log.presentation.training_log.TrainingLogEvent
import com.example.scarlet.ui.theme.MainButtonContentPadding
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun BlockListSection(
    navigator: DestinationsNavigator,
    blocks: List<BlockWithDays<DayWithSessions<Session>>> = emptyList(),
    onEvent: (TrainingLogEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        /*************************************************************************
         * Latest training block
         *************************************************************************/
        blocks.firstOrNull()?.let { latestBlock ->
            item {
                SectionTitle(stringResource(R.string.latest_training_block))

                val blockSessions = latestBlock.days.flatMap { it.sessions }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = MainButtonContentPadding,
                    shape = MaterialTheme.shapes.large,
                    onClick = {
                        navigator.navigate(BlockScreenDestination(latestBlock.toBlock()))
                    }
                ) {
                    DeletableItem(
                        modifier = Modifier.fillMaxSize(),
                        onDeleteClicked = {
                            onEvent(TrainingLogEvent.DeleteBlock(latestBlock.toBlock()))
                        }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = latestBlock.name,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = if (blockSessions.isNotEmpty()) {
                                    stringResource(
                                        R.string.block_started_on,
                                        DateUtils.formatDate(blockSessions.first().date)
                                    )
                                } else {
                                    stringResource(R.string.empty_block)
                                },
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(8.dp)) }

            /*************************************************************************
             * Preceding training blocks
             *************************************************************************/
            if (blocks.size > 1) {
                item {
                    SectionTitle(stringResource(R.string.preceding_training_blocks))
                }
                items(blocks.subList(1, blocks.size)) { block ->
                    val blockSessions = block.days.flatMap { it.sessions }
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = MainButtonContentPadding,
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        onClick = {
                            navigator.navigate(BlockScreenDestination(block.toBlock()))
                        }
                    ) {
                        DeletableItem(
                            modifier = Modifier.fillMaxSize(),
                            onDeleteClicked = {
                                onEvent(TrainingLogEvent.DeleteBlock(block.toBlock()))
                            }
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = block.name,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = if (blockSessions.isNotEmpty()) {
                                        DateUtils.formatDate(blockSessions.first().date) + " - " +
                                                DateUtils.formatDate(blockSessions.last().date)
                                    } else {
                                        stringResource(R.string.empty_block)
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                    }
                }
            }

        } ?: run {
            item {
                Text(
                    text = stringResource(R.string.no_training_blocks),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = stringResource(R.string.start_new_block),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // Give some space at the bottom of the lazy column
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}


@Composable
fun SectionTitle(
    title: String
) {
    // TODO choose a lighter color shade
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge
    )
}