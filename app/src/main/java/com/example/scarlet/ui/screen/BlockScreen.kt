package com.example.scarlet.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scarlet.R
import com.example.scarlet.db.model.Block
import com.example.scarlet.db.model.Session
import com.example.scarlet.ui.composables.ScarletList
import com.example.scarlet.ui.events.BlockEvent
import com.example.scarlet.ui.navigation.BlockScreenNavArgs
import com.example.scarlet.ui.screen.destinations.SessionScreenDestination
import com.example.scarlet.ui.states.BlockUiState
import com.example.scarlet.ui.theme.ScarletTheme
import com.example.scarlet.viewmodel.BlockViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Destination(
    navArgsDelegate = BlockScreenNavArgs::class
)
@Composable
fun BlockScreen(
    navigator: DestinationsNavigator
) {
    val blockViewModel: BlockViewModel = hiltViewModel()
    val state by blockViewModel.state.collectAsState()

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
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BlockHeader(
                state = state,
                onEvent = onEvent
            )
            Spacer(modifier = Modifier.height(16.dp))
            SessionsSection(
                navigator = navigator,
                onEvent = onEvent,
                sessions = state.sessions,
            )
        }
    }
}

@Composable
fun BlockHeader(
    state: BlockUiState,
    onEvent: (BlockEvent) -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = state.block.name,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            Button(onClick = {
                onEvent(BlockEvent.AddSession)
            }) {
                Text(text = stringResource(id = R.string.new_session))
            }
            /* TODO : check if the block is already completed */
            Button(onClick = {
                onEvent(BlockEvent.EndBlock)
            }) {
                Text(text = stringResource(id = R.string.end_block))
            }
        }
    }
}

@Composable
fun SessionsSection(
    navigator: DestinationsNavigator,
    onEvent: (BlockEvent) -> Unit,
    sessions: List<Session>
) {
    ScarletList(
        modifier = Modifier.fillMaxWidth(),
        title = stringResource(R.string.block_sessions_list_title),
        items = sessions,
        onItemClicked = { session ->
            navigator.navigate(SessionScreenDestination(session = session))
        },
        onDeleteClicked = { session ->
            onEvent(BlockEvent.DeleteSession(session))
        }
    ) { session ->
        Text(text = session.date)
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