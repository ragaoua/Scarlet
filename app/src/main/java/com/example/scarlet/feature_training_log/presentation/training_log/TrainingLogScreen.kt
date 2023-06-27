package com.example.scarlet.feature_training_log.presentation.training_log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.presentation.components.ScarletList
import com.example.scarlet.feature_training_log.presentation.destinations.BlockScreenDestination
import com.example.scarlet.ui.theme.ScarletTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Destination
@Composable
fun TrainingLogScreen(
    navigator: DestinationsNavigator
) {
    val trainingLogViewModel: TrainingLogViewModel = hiltViewModel()
    val state by trainingLogViewModel.state.collectAsState()

    Screen(
        navigator = navigator,
        state = state,
        onEvent = trainingLogViewModel::onEvent
    )
}

@Composable
fun Screen(
    navigator: DestinationsNavigator,
    state: TrainingLogUiState,
    onEvent: (TrainingLogEvent) -> Unit
) {
    ScarletTheme {
        if(state.isAddingBlock) {
            NewBlockDialog(
                state = state,
                onEvent = onEvent
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.training_log),
                fontSize = 48.sp,
                modifier = Modifier.padding(top = 32.dp, bottom = 32.dp)
            ) // TODO : make into a "screen title" function ?
            ActiveBlockSection(
                navigator = navigator,
                state = state,
                onEvent = onEvent
            )
            Spacer(modifier = Modifier.height(16.dp))
            CompletedBlocksSection(
                navigator = navigator,
                state = state,
                onEvent = onEvent
            )
        }
    }
}

@Composable
fun ActiveBlockSection(
    navigator: DestinationsNavigator,
    state: TrainingLogUiState,
    onEvent: (TrainingLogEvent) -> Unit
) {
    state.activeBlock?.let {activeBlock ->
        ScarletList(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.active_training_block),
            items = listOf(activeBlock),
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
                    text = "Started on XX/XX/XXXX", /* TODO*/
                    style = MaterialTheme.typography.bodySmall
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

@Composable
fun CompletedBlocksSection(
    navigator: DestinationsNavigator,
    state: TrainingLogUiState,
    onEvent: (TrainingLogEvent) -> Unit
) {
    ScarletList(
        modifier = Modifier.fillMaxWidth(),
        title = stringResource(R.string.completed_training_blocks),
        items = state.completedBlocks,
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
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBlockDialog(
    state: TrainingLogUiState,
    onEvent: (TrainingLogEvent) -> Unit
) {
    var blockName by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = {
            onEvent(TrainingLogEvent.ShowNewBlockDialog)
        },
        title = {
            Text(stringResource(R.string.new_block))
        },
        text  = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if(state.isShowingBlockNameEmptyMsg) {
                    Text(stringResource(R.string.block_name_empty))
                }
                TextField(
                    value = blockName,
                    onValueChange = { blockName = it }
                )
            }

        },
        confirmButton = {
            Button(onClick = {
                onEvent(TrainingLogEvent.CreateBlock(blockName))
            }) {
                Text(stringResource(R.string.create_block))
            }
        },
        dismissButton = {
            Button(onClick = {
                onEvent(TrainingLogEvent.HideNewBlockDialog)
            }) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewNoBlocks() {
    Screen(
        navigator = EmptyDestinationsNavigator,
        state = TrainingLogUiState(),
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTrainingLogScreen() {
    Screen(
        navigator = EmptyDestinationsNavigator,
        state = TrainingLogUiState(
            activeBlock = Block(name = "Block 5"),
            completedBlocks = listOf(
                Block(name = "Block 4", completed = true),
                Block(name = "Block 3", completed = true),
                Block(name = "Block 2", completed = true),
                Block(name = "Block 1", completed = true)
            )
        ),
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewNewBlockDialog() {
    Screen(
        navigator = EmptyDestinationsNavigator,
        state = TrainingLogUiState(
            isAddingBlock = true
        ),
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewNewBlockDialogEmptyBlockName() {
    Screen(
        navigator = EmptyDestinationsNavigator,
        state = TrainingLogUiState(
            isAddingBlock = true,
            isShowingBlockNameEmptyMsg = true
        ),
        onEvent = {}
    )
}