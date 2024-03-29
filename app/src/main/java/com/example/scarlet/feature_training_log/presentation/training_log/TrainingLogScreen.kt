package com.example.scarlet.feature_training_log.presentation.training_log

import NewBlockSheet
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.destinations.BlockScreenDestination
import com.example.scarlet.feature_training_log.presentation.training_log.components.BlockListSection
import com.example.scarlet.ui.theme.ScarletTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@RootNavGraph(start = true)
@Destination
@Composable
fun TrainingLogScreen(
    navigator: DestinationsNavigator
) {
    val trainingLogViewModel: TrainingLogViewModel = hiltViewModel()
    val state by trainingLogViewModel.state.collectAsStateWithLifecycle()

    Screen(
        navigator = navigator,
        state = state,
        onEvent = trainingLogViewModel::onEvent,
        uiActions = trainingLogViewModel.uiActions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(
    navigator: DestinationsNavigator,
    state: TrainingLogUiState,
    onEvent: (TrainingLogEvent) -> Unit,
    uiActions: Flow<TrainingLogViewModel.UiAction>
) {
    /************************************************************************
     * Treating UI actions from the ViewModel
     ************************************************************************/
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(true) {
        uiActions.collect { action ->
            when(action) {
                is TrainingLogViewModel.UiAction.NavigateToBlockScreen -> {
                    navigator.navigate(BlockScreenDestination(action.block))
                }
                is TrainingLogViewModel.UiAction.ShowSnackbar -> {
                    coroutineScope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        val snackbarResult = snackbarHostState.showSnackbar(
                            message = context.getString(action.message.resId, *action.message.args),
                            actionLabel = action.actionLabel?.let {
                                context.getString(it.resId, *it.args)
                            },
                            duration = SnackbarDuration.Short
                        )
                        action.onActionPerformed?.let { onActionPerformed ->
                            when (snackbarResult) {
                                SnackbarResult.ActionPerformed -> {
                                    onActionPerformed()
                                }
                                SnackbarResult.Dismissed -> Unit
                            }
                        }
                    }
                }
            }
        }
    }

    /************************************************************************
     * Actual screen
     ************************************************************************/
    ScarletTheme {
        val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
            topBar = { TrainingLogTopAppBar(topAppBarScrollBehavior) },
            floatingActionButton = { AddBlockButton(onEvent) },
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                        actionColor = MaterialTheme.colorScheme.primary,
                        snackbarData = data
                    )
                }
            }
        ) { innerPadding ->
            Surface (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                BlockListSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    navigator = navigator,
                    blocks = state.blocks,
                    onEvent = onEvent
                )
            }
        }

        state.newBlockSheetState?.let {
            NewBlockSheet(
                sheetState = it,
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun AddBlockButton(onEvent: (TrainingLogEvent) -> Unit) {
    FloatingActionButton(
        onClick = { onEvent(TrainingLogEvent.ShowNewBlockSheet) }
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(id = R.string.add_new_block)
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TrainingLogTopAppBar(
    topAppBarScrollBehavior: TopAppBarScrollBehavior
) {
    LargeTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.training_log),
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        scrollBehavior = topAppBarScrollBehavior
    )
}
