package com.example.scarlet.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import com.example.scarlet.db.model.Block
import com.example.scarlet.ui.composables.ScarletClickableItem
import com.example.scarlet.ui.events.TrainingLogEvent
import com.example.scarlet.ui.screen.destinations.BlockScreenDestination
import com.example.scarlet.ui.states.TrainingLogUiState
import com.example.scarlet.ui.theme.ScarletTheme
import com.example.scarlet.viewmodel.TrainingLogViewModel
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
            ) /* TODO : make into a "screen title" function ? */
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
    Column (
        modifier = Modifier.fillMaxWidth()
    ) {
        BlockSectionTitle(
            title = stringResource(R.string.active_training_block)
        )
        state.activeBlock?.let { activeBlock ->
            BlockList(
                navigator = navigator,
                blocks = listOf(activeBlock),
                onEvent = onEvent
            )
        }?: run {
            NewBlockButton(
                onEvent = onEvent
            )
        }
    }
}

@Composable
fun CompletedBlocksSection(
    navigator: DestinationsNavigator,
    state: TrainingLogUiState,
    onEvent: (TrainingLogEvent) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BlockSectionTitle(
            stringResource(R.string.completed_training_blocks)
        )
        BlockList(
            navigator = navigator,
            state.completedBlocks,
            onEvent = onEvent
        )
    }
}

@Composable
fun BlockList(
    navigator: DestinationsNavigator,
    blocks: List<Block>,
    onEvent: (TrainingLogEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        blocks.forEach { block ->
            BlockButton(
                navigator = navigator,
                block = block,
                onEvent = onEvent
            )
        }
    }
}

@Composable
fun BlockSectionTitle(
    title: String
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = title,
        fontSize = 20.sp
    )
}

@Composable
fun BlockButton(
    navigator: DestinationsNavigator,
    block: Block,
    onEvent: (TrainingLogEvent) -> Unit
) {
    ScarletClickableItem(
        onClick = {
            navigator.navigate(BlockScreenDestination(block = block))
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column() {
                Text(
                    text = block.name,
                    fontSize = 16.sp
                )
                Text(
                    text = "Started on XX/XX/XXXX", /* TODO*/
                    fontSize = 10.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete block", /* TODO : create resource */
                modifier = Modifier.clickable {
                    onEvent(TrainingLogEvent.DeleteBlock(block))
                }
            )
        }
    }
}

@Composable
fun NewBlockButton(
    onEvent: (TrainingLogEvent) -> Unit
) {
    ScarletClickableItem(
        onClick = {
            onEvent(TrainingLogEvent.ShowNewBlockDialog)
        }
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add new block" /* TODO : create resource */
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = stringResource(R.string.no_active_block_msg),
                    fontSize = 16.sp
                )
                Text(
                    text = stringResource(R.string.start_new_block_msg),
                    fontSize = 10.sp
                )
            }
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
                Block(name = "Block 1"),
                Block(name = "Block 2"),
                Block(name = "Block 3"),
                Block(name = "Block 4")
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