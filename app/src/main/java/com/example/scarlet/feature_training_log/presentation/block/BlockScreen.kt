package com.example.scarlet.feature_training_log.presentation.block

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.SessionWithMovements
import com.example.scarlet.feature_training_log.presentation.block.components.BlockHeader
import com.example.scarlet.feature_training_log.presentation.block.components.SessionsList
import com.example.scarlet.feature_training_log.presentation.destinations.SessionScreenDestination
import com.example.scarlet.ui.theme.ScarletTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import java.util.Date

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
        blockViewModel.channel.collectLatest { action ->
            when(action) {
                is BlockViewModel.UiAction.NavigateUp -> {
                    navigator.navigateUp()
                }
                is BlockViewModel.UiAction.NavigateToSessionScreen -> {
                    navigator.navigate(SessionScreenDestination(
                        session = action.session,
                        block = state.block
                    ))
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
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                BlockHeader(
                    block = state.block,
                    isEditing = state.isInEditMode,
                    onEvent = onEvent
                )
                Spacer(modifier = Modifier.height(64.dp))
                SessionsList(
                    navigator = navigator,
                    state = state,
                    onEvent = onEvent
                )
            }
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
            sessionsWithMovements = listOf(
                SessionWithMovements(
                    Session(
                        date = Date(System.currentTimeMillis()),
                        blockId = 1
                    ),
                    emptyList()
                ),
                SessionWithMovements(
                    Session(
                        date = Date(System.currentTimeMillis()),
                        blockId = 1
                    ),
                    listOf(
                        Movement(name = "Bench Press"),
                        Movement(name = "Comp Squat"),
                        Movement(name = "Dips"),
                    )
                ),
            )
        ),
        onEvent = {}
    )
}