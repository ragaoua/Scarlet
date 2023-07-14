package com.example.scarlet.feature_training_log.presentation.training_log

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.BlockWithSessions
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.presentation.destinations.BlockScreenDestination
import com.example.scarlet.feature_training_log.presentation.training_log.components.ActiveBlockSection
import com.example.scarlet.feature_training_log.presentation.training_log.components.CompletedBlocksSection
import com.example.scarlet.feature_training_log.presentation.training_log.components.NewBlockDialog
import com.example.scarlet.ui.theme.ScarletTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Destination
@Composable
fun TrainingLogScreen(
    navigator: DestinationsNavigator
) {
    val trainingLogViewModel: TrainingLogViewModel = hiltViewModel()
    val state by trainingLogViewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        trainingLogViewModel.event.collectLatest { event ->
            when(event) {
                is TrainingLogViewModelUiEvent.NavigateToBlockScreen -> {
                    navigator.navigate(BlockScreenDestination(event.block))
                }
            }
        }
    }

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
        Surface (
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (state.isAddingBlock) {
                NewBlockDialog(
                    state = state,
                    onEvent = onEvent
                )
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(64.dp))
                Text(
                    text = stringResource(id = R.string.training_log),
                    style = MaterialTheme.typography.displayMedium
                )

                Spacer(modifier = Modifier.height(32.dp))
                ActiveBlockSection(
                    navigator = navigator,
                    activeBlock = state.activeBlock,
                    onEvent = onEvent
                )

                Spacer(modifier = Modifier.height(16.dp))
                CompletedBlocksSection(
                    navigator = navigator,
                    completedBlocks = state.completedBlocks,
                    onEvent = onEvent
                )
            }
        }
    }
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
            activeBlock =
                BlockWithSessions(
                    block = Block(name = "Block 5"),
                    emptyList()
                ),
            completedBlocks = listOf(
                BlockWithSessions(
                    block = Block(name = "Block 1"),
                    listOf(
                        Session(date = "01/01/2021"),
                        Session(date = "01/02/2021")
                    )
                ),
                BlockWithSessions(
                    block = Block(name = "Block 2"),
                    listOf(
                        Session(date = "01/01/2021"),
                        Session(date = "10/02/2021")
                    )
                )
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