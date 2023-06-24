package com.example.scarlet.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scarlet.R
import com.example.scarlet.events.TrainingLogEvent
import com.example.scarlet.db.model.Block
import com.example.scarlet.ui.states.TrainingLogUiState
import com.example.scarlet.ui.screen.destinations.BlockScreenDestination
import com.example.scarlet.ui.theme.ScarletTheme
import com.example.scarlet.viewmodel.TrainingLogViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

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
                onEvent = onEvent
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.training_log),
                fontSize = 48.sp
            )
            ActiveBlockSection(
                navigator = navigator,
                state = state,
                onEvent = onEvent
            )
            CompletedBlocksSection(
                navigator = navigator,
                state = state
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
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.active_training_block),
            fontSize = 20.sp
        )
        state.activeBlock?.let { activeBlock ->
            BlockButton(
                navigator = navigator,
                block = activeBlock)
        }?: run {
            AddBlockButton(
                onEvent = onEvent
            )
        }
    }
}

@Composable
fun CompletedBlocksSection(
    navigator: DestinationsNavigator,
    state: TrainingLogUiState
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.completed_training_blocks),
            fontSize = 20.sp
        )
        state.completedBlocks.forEach { block ->
            BlockButton(
                navigator = navigator,
                block = block
            )
        }
    }
}

@Composable
fun BlockButton(
    navigator: DestinationsNavigator,
    block: Block
) {
    Button(onClick = {
        navigator.navigate(BlockScreenDestination(block = block))
    }) {
        Text(block.name)
    }
}

@Composable
fun AddBlockButton(
    onEvent: (TrainingLogEvent) -> Unit
) {
    Button(onClick = {
        onEvent(TrainingLogEvent.ShowNewBlockDialog)
    }) {
        Text(text = stringResource(R.string.no_active_block_msg))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBlockDialog(
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
            TextField(
                value = blockName,
                onValueChange = { blockName = it }
            )
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
