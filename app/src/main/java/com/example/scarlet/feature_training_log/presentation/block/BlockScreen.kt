package com.example.scarlet.feature_training_log.presentation.block

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.presentation.block.components.BlockHeader
import com.example.scarlet.feature_training_log.presentation.block.components.SessionsList
import com.example.scarlet.ui.theme.ScarletTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Destination(
    navArgsDelegate = BlockScreenNavArgs::class
)
@Composable
fun BlockScreen(
    navigator: DestinationsNavigator
) {
    val blockViewModel: BlockViewModel = hiltViewModel()
    val state by blockViewModel.state.collectAsState()
    LaunchedEffect(key1 = true) {
        blockViewModel.event.collectLatest { event ->
            when(event) {
                is UiEvent.NavigateUp -> {
                    navigator.navigateUp()
                }
            }
        }
    }

    Screen(
        navigator = navigator,
        state = state,
        onEvent = blockViewModel::onEvent
    )
}

@Composable
fun Screen(
    navigator: DestinationsNavigator,
    state: BlockUiState,
    onEvent: (BlockEvent) -> Unit
) {
    ScarletTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BlockHeader(
                block = state.block,
                onEvent = onEvent
            )
            Spacer(modifier = Modifier.height(16.dp))
            SessionsList(
                navigator = navigator,
                sessions = state.sessions,
                onEvent = onEvent
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNoSessions() {
    Screen(
        navigator = EmptyDestinationsNavigator,
        state = BlockUiState(
            block = Block(
                name = "Block 1",
            )
        ),
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewBlockScreen() {
    Screen(
        navigator = EmptyDestinationsNavigator,
        state = BlockUiState(
            block = Block(
                name = "Block 1",
            ),
            sessions = listOf(
                Session(
                    date = "24-06-2023",
                    blockId = 1
                ),
                Session(
                    date = "21-06-2023",
                    blockId = 1
                ),
            )
        ),
        onEvent = {}
    )
}