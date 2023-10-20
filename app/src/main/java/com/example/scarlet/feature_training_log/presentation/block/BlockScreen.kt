package com.example.scarlet.feature_training_log.presentation.block

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.block.components.EditBlockSheet
import com.example.scarlet.feature_training_log.presentation.block.components.EditMovementSheet
import com.example.scarlet.feature_training_log.presentation.block.components.LoadCalculationDialog
import com.example.scarlet.feature_training_log.presentation.block.components.MovementSelectionSheet
import com.example.scarlet.feature_training_log.presentation.block.components.Session
import com.example.scarlet.feature_training_log.presentation.block.components.SessionDatePickerDialog
import com.example.scarlet.ui.theme.ScarletTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@Destination(navArgsDelegate = BlockScreenNavArgs::class)
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Screen(
    navigator: DestinationsNavigator,
    state: BlockUiState,
    onEvent: (BlockEvent) -> Unit,
    uiActions: Flow<BlockViewModel.UiAction>
) {
    /************************************************************************
     * Processing UI actions from the ViewModel
     ************************************************************************/
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(true) {
        uiActions.collect { action ->
            when(action) {
                is BlockViewModel.UiAction.NavigateUp -> {
                    navigator.navigateUp()
                }
                is BlockViewModel.UiAction.ShowSnackbar -> {
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
     * Back button handling
     ************************************************************************/
    BackHandler(
        enabled = state.isInSessionEditMode
    ) {
        onEvent(BlockEvent.ToggleSessionEditMode)
    }

    /************************************************************************
     * Actual screen
     ************************************************************************/
    ScarletTheme {
        val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        Scaffold(
            // Modifier.safeDrawingPadding is needed to ensure that content isn't obscured
            // and elements don't overlap with the system UI, since we're using
            // WindowCompat.setDecorFitsSystemWindows(window, false) in MainActivity.
            // See https://developer.android.com/jetpack/compose/layouts/insets#insets-setup
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                .safeDrawingPadding(),
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                        actionColor = MaterialTheme.colorScheme.primary,
                        snackbarData = data
                    )
                }
            },
            topBar = { BlockTopAppBar(state, onEvent, topAppBarScrollBehavior) },
            bottomBar = { BottomBar(state, onEvent) },
            floatingActionButton = { BlockFloatingActionButtons(state, onEvent) }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                AnimatedContent(
                    targetState = state.selectedDayId,
                    label = "day selection animation"
                ) { selectedDayId ->
                    val day = state.days.find { it.id == selectedDayId }

                    val lazyListState = sessionsLazyListState(state, onEvent)
                    LazyRow(
                        state = lazyListState,
                        flingBehavior = rememberSnapFlingBehavior(lazyListState),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val sessions = day?.sessions ?: emptyList()
                        if (sessions.isNotEmpty()) {
                            items(
                                items = sessions,
                                key = { it.id }
                            ) { session ->
                                Session(
                                    modifier = Modifier
                                        .fillParentMaxWidth()
                                        .animateItemPlacement()
                                        .padding(8.dp),
                                    session = session,
                                    isExerciseDetailExpandedById = state.isExerciseDetailExpandedById,
                                    expandedDropdownMenuExerciseId = state.expandedDropdownMenuExerciseId,
                                    isInSessionEditMode = state.isInSessionEditMode,
                                    selectedSet = state.setTextField?.set,
                                    selectedSetField = state.setTextField?.field,
                                    onEvent = onEvent
                                )
                            }
                        } else {
                            item {
                                Text(
                                    text = stringResource(R.string.no_sessions_yet),
                                    style = MaterialTheme.typography.bodyMedium
                                    // TODO use a lighter color
                                )
                            }
                        }
                    }
                }
            }
        }

        state.editBlockSheet?.let {
            EditBlockSheet(
                sheetState = it,
                onEvent = onEvent
            )
        }

        state.sessionDatePickerDialog?.let {
            SessionDatePickerDialog(
                session = it.session,
                onEvent = onEvent
            )
        }

        state.movementSelectionSheet?.let { sheetState ->
            sheetState.editMovementSheet?.let {
                EditMovementSheet(
                    sheetState = it,
                    onEvent = onEvent
                )
            } ?: run {
                MovementSelectionSheet(
                    sheetState = sheetState,
                    onEvent = onEvent,
                )
            }
        }

        state.loadCalculationDialog?.let {
            LoadCalculationDialog(
                dialogState = it,
                onEvent = onEvent
            )
        }
    }
}

@OptIn(FlowPreview::class)
@Composable
private fun sessionsLazyListState(
    state: BlockUiState,
    onEvent: (BlockEvent) -> Unit
): LazyListState {
    val sessionIndexScrollPosition =
        state.sessionIndexScrollPositionByDayId[state.selectedDayId] ?: 0

    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = sessionIndexScrollPosition
    ) // Restore the saved position when this is recomposed

    LaunchedEffect(sessionIndexScrollPosition) {
        lazyListState.animateScrollToItem(sessionIndexScrollPosition)
    } // Animate the scroll position when the a session is added or removed

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .debounce(500L)
            .collectLatest {
                if(!lazyListState.isScrollInProgress)
                    onEvent(BlockEvent.UpdateSessionIndexScrollPosition(it))
            }
    } // Save the scroll position for when this is recomposed

    return lazyListState
}

@Composable
private fun BlockFloatingActionButtons(
    state: BlockUiState,
    onEvent: (BlockEvent) -> Unit
) {
    AnimatedVisibility(
        visible = state.areFloatingActionButtonsVisible && state.setTextField == null,
        enter = slideInVertically { it } + scaleIn(),
        exit = slideOutVertically { it } + scaleOut()
    ) {
        Row {
            AnimatedVisibility(
                visible = state.days.flatMap { it.sessions }.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FloatingActionButton(
                    onClick = { onEvent(BlockEvent.ToggleSessionEditMode) },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ) {
                    Icon(
                        imageVector = if (state.isInSessionEditMode) {
                            Icons.Default.Check
                        } else Icons.Default.Edit,
                        contentDescription = stringResource(id = R.string.edit_sessions)
                    )
                }
            }
            AnimatedVisibility(visible = !state.isInSessionEditMode) {
                FloatingActionButton(
                    onClick = { onEvent(BlockEvent.AddSession) },
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.new_session)
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BlockTopAppBar(
    state: BlockUiState,
    onEvent: (BlockEvent) -> Unit,
    topAppBarScrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = {
            Text(
                text = state.block.name,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        actions = {
            IconButton(
                onClick = { onEvent(BlockEvent.EditBlock) }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_block)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ),
        scrollBehavior = topAppBarScrollBehavior
    )
}

@Composable
private fun BottomBar(
    state: BlockUiState,
    onEvent: (BlockEvent) -> Unit
) {
    AnimatedContent(
        targetState = state.setTextField != null,
        label = "Bottom bar animation",
        transitionSpec = {
            slideInVertically { it } togetherWith slideOutVertically { it }
        }
    ) { isSetTextFieldVisible ->
        // state.setTextField != null is necessary to allow smart cast of
        // state.setTextField (SetTextField?) to SetTextField and avoid using
        // the !! operator
        if (isSetTextFieldVisible && state.setTextField != null) {
            SetTextField(state.setTextField, onEvent)
        } else {
            DayNavigationBottomBar(state, onEvent)
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun SetTextField(
    setTextField: BlockUiState.SetTextField,
    onEvent: (BlockEvent) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    SideEffect { focusRequester.requestFocus() }

    var hasImeBeenVisible by remember { mutableStateOf(false) }
    val isImeVisible = WindowInsets.isImeVisible
    LaunchedEffect(isImeVisible) {
        if (isImeVisible) {
            hasImeBeenVisible = true
        } else if (hasImeBeenVisible) {
            onEvent(BlockEvent.HideSetTextField)
        }
    }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = setTextField.value,
        onValueChange = { onEvent(BlockEvent.UpdateSetFieldValue(it)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Decimal,
            imeAction = if (setTextField.isLastField) {
                ImeAction.Done
            } else ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onAny = { onEvent(BlockEvent.UpdateSet) }
        )
    )
}

@Composable
private fun DayNavigationBottomBar(
    state: BlockUiState,
    onEvent: (BlockEvent) -> Unit
) {
    if (state.days.size <= 1) return

    NavigationBar {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                16.dp,
                Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(state.days) { day ->
                Column(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable { onEvent(BlockEvent.SelectDay(day.toDay())) }
                        .background(
                            if (day.id == state.selectedDayId) {
                                MaterialTheme.colorScheme.primary
                            } else Color.Transparent
                        )
                        .padding(4.dp)
                        .widthIn(64.dp, 128.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.day, day.order),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = pluralStringResource(
                            id = R.plurals.nb_sessions,
                            count = day.sessions.size,
                            day.sessions.size
                        ),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}