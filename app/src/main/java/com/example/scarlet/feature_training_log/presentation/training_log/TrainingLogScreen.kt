package com.example.scarlet.feature_training_log.presentation.training_log

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.destinations.BlockScreenDestination
import com.example.scarlet.feature_training_log.presentation.training_log.components.ActiveBlockSection
import com.example.scarlet.feature_training_log.presentation.training_log.components.CompletedBlocksSection
import com.example.scarlet.feature_training_log.presentation.training_log.components.NewBlockSheet
import com.example.scarlet.ui.theme.ScarletTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.Flow

@Destination
@Composable
fun TrainingLogScreen(
    navigator: DestinationsNavigator
) {
    val trainingLogViewModel: TrainingLogViewModel = hiltViewModel()
    val state by trainingLogViewModel.state.collectAsStateWithLifecycle()
    val uiActions = trainingLogViewModel.uiActions

    LaunchedEffect(true) {
        uiActions.collect { action ->
            when(action) {
                is TrainingLogViewModel.UiAction.NavigateToBlockScreen -> {
                    navigator.navigate(BlockScreenDestination(action.block))
                }
                else -> Unit
            }
        }
    }

    Screen(
        navigator = navigator,
        state = state,
        onEvent = trainingLogViewModel::onEvent,
        uiActions = uiActions
    )
}

@Composable
fun Screen(
    navigator: DestinationsNavigator,
    state: TrainingLogUiState,
    onEvent: (TrainingLogEvent) -> Unit,
    uiActions: Flow<TrainingLogViewModel.UiAction>
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(true) {
        uiActions.collect { action ->
            when(action) {
                is TrainingLogViewModel.UiAction.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(action.error.resId, action.error.args)
                    )
                }
                else -> Unit
            }
        }
    }
    ScarletTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { innerPadding ->
            Surface (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
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
                        activeBlockWithSessions = state.activeBlock,
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
        if(state.isNewBlockSheetExpanded) {
            NewBlockSheet(onEvent = onEvent)
        }
    }
}
