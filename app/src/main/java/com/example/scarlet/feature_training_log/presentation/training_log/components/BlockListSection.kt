package com.example.scarlet.feature_training_log.presentation.training_log.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.BlockWithSessions
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.presentation.core.DateUtils
import com.example.scarlet.feature_training_log.presentation.destinations.BlockScreenDestination
import com.example.scarlet.feature_training_log.presentation.training_log.TrainingLogEvent
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun BlockListSection(
    modifier: Modifier,
    navigator: DestinationsNavigator,
    blocks: List<BlockWithSessions<Session>> = emptyList(),
    onEvent: (TrainingLogEvent) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        /*************************************************************************
         * Latest training block
         *************************************************************************/
        blocks.firstOrNull()?.let { latestBlock ->
            item {
                SectionTitle(stringResource(R.string.latest_training_block))
            }
            item {
                BlockButton(
                    navigator = navigator,
                    block = latestBlock,
                    blockSubtext = if (latestBlock.sessions.isNotEmpty()) {
                        stringResource(
                            R.string.block_started_on,
                            DateUtils.formatDate(latestBlock.sessions.first().date)
                        )
                    } else {
                        stringResource(R.string.empty_block)
                    },
                    onEvent = onEvent
                )
            }

            /*************************************************************************
             * Preceding training blocks
             *************************************************************************/
            if (blocks.size > 1) {
                item {
                    SectionTitle(stringResource(R.string.preceding_training_blocks))
                }
                items(blocks.subList(1, blocks.size)) { block ->
                    BlockButton(
                        navigator = navigator,
                        block = block,
                        blockSubtext = if (block.sessions.isNotEmpty()) {
                            DateUtils.formatDate(block.sessions.first().date) + " - " +
                                    DateUtils.formatDate(block.sessions.last().date)
                        } else {
                            stringResource(R.string.empty_block)
                        },
                        onEvent = onEvent,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    )
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
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun BlockButton(
    navigator: DestinationsNavigator,
    block: BlockWithSessions<Session>,
    blockSubtext: String,
    onEvent: (TrainingLogEvent) -> Unit,
    colors: ButtonColors = ButtonDefaults.buttonColors()
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            vertical = 20.dp,
            horizontal = 16.dp
        ),
        shape = MaterialTheme.shapes.large,
        colors = colors,
        onClick = { navigator.navigate(BlockScreenDestination(block.toBlock())) }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            var iconButtonWidth by remember { mutableStateOf(0.dp) }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = iconButtonWidth),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = block.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = blockSubtext,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            val localDensity = LocalDensity.current
            IconButton(
                onClick = { onEvent(TrainingLogEvent.DeleteBlock(block.toBlock())) },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .onGloballyPositioned {
                        iconButtonWidth = with(localDensity) {
                            it.size.width.toDp()
                        }
                    }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}