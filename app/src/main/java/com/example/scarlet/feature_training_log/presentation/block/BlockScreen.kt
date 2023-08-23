package com.example.scarlet.feature_training_log.presentation.block

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scarlet.feature_training_log.presentation.block.components.BlockHeader
import com.example.scarlet.feature_training_log.presentation.block.components.SessionsList
import com.example.scarlet.feature_training_log.presentation.destinations.SessionScreenDestination
import com.example.scarlet.ui.theme.ScarletTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.Flow

@Destination(
    navArgsDelegate = BlockScreenNavArgs::class
)
@Composable
fun BlockScreen(
    navigator: DestinationsNavigator
) {
    val blockViewModel: BlockViewModel = hiltViewModel()
    val state by blockViewModel.state.collectAsStateWithLifecycle()

    Screen(
        navigator = navigator,
        state = state,
        onEvent = blockViewModel::onEvent,
        uiActions = blockViewModel.uiActions
    )
}

@Composable
fun Screen(
    navigator: DestinationsNavigator,
    state: BlockUiState,
    onEvent: (BlockEvent) -> Unit,
    uiActions: Flow<BlockViewModel.UiAction>
) {
    /************************************************************************
     * Treating UI actions from the ViewModel
     ************************************************************************/
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(true) {
        uiActions.collect { action ->
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
                is BlockViewModel.UiAction.ShowSnackbarWithError -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(action.error.resId, *action.error.args)
                    )
                }
            }
        }
    }

    /************************************************************************
     * Actual screen
     ************************************************************************/
    ScarletTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                NavigationBar {
                    LazyRow (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(state.days) {
                            Text(
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.small)
                                    .clickable {
                                        onEvent(BlockEvent.SelectDay(it.day.id))
                                    }
                                    .padding(4.dp)
                                    .widthIn(64.dp, 128.dp),
                                text = it.day.name,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(48.dp))
                    BlockHeader(
                        state = state,
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
}