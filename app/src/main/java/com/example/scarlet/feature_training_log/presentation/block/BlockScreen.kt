package com.example.scarlet.feature_training_log.presentation.block

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.presentation.block.components.LoadCalculationDialog
import com.example.scarlet.feature_training_log.presentation.block.components.MovementSelectionSheet
import com.example.scarlet.feature_training_log.presentation.block.components.SessionsList
import com.example.scarlet.feature_training_log.presentation.core.components.AddEditBlockSheet
import com.example.scarlet.feature_training_log.presentation.core.components.AddEditBlockSheetState
import com.example.scarlet.ui.theme.ScarletTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.Flow
import java.util.Date

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

@OptIn(ExperimentalMaterial3Api::class)
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
    LaunchedEffect(true) {
        uiActions.collect { action ->
            when(action) {
                is BlockViewModel.UiAction.NavigateUp -> {
                    navigator.navigateUp()
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
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = { BlockTopAppBar(state, onEvent, topAppBarScrollBehavior) },
            bottomBar = { DayNavigationBottomBar(state, onEvent) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onEvent(BlockEvent.AddSession) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.new_session)
                    )
                }
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                SessionsList(
                    state = state,
                    onEvent = onEvent
                )
            }
        }

        state.editBlockSheet?.let{
            AddEditBlockSheet(
                sheetState = AddEditBlockSheetState(
                    isNewBlock = false,
                    blockName = it.blockName,
                    blockNameError = it.blockNameError?.let { error ->
                        stringResource(error.resId, *error.args)
                    },
                    areMicroCycleSettingsExpanded = it.areMicroCycleSettingsExpanded,
                    daysPerMicroCycle = it.daysPerMicroCycle
                ),
                onBlockNameValueChange = { value ->
                    onEvent(BlockEvent.UpdateEditedBlockName(value))
                },
                onMicroCycleSettingsToggle = {
                    onEvent(BlockEvent.ToggleMicroCycleSettings)
                },
                onDaysPerMicroCycleValueChange = { value ->
                    onEvent(BlockEvent.UpdateDaysPerMicroCycle(value))
                },
                onDismissRequest = {
                    onEvent(BlockEvent.CancelBlockEdition)
                },
                onValidate = { onEvent(BlockEvent.SaveBlockName) }
            )
        }

        state.sessionDatePickerDialog?.let{
            SessionDatePickerDialog(
                session = it.session,
                onEvent = onEvent
            )
        }

        state.movementSelectionSheet?.let {
            MovementSelectionSheet(
                movements = state.movements,
                movementNameFilter = it.movementNameFilter,
                onEvent = onEvent,
            )
        }

        state.loadCalculationDialog?.let {
            LoadCalculationDialog(
                dialogState = it,
                onEvent = onEvent
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SessionDatePickerDialog(
    session: Session,
    onEvent: (BlockEvent) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = session.date.time
    )
    DatePickerDialog(
        onDismissRequest = { onEvent(BlockEvent.HideSessionDatePickerDialog) },
        confirmButton = {
            Button(
                onClick = {
                    datePickerState.selectedDateMillis?.let { dateMillis ->
                        onEvent(BlockEvent.UpdateSessionDate(Date(dateMillis)))
                    }
                },
                enabled = datePickerState.selectedDateMillis != null
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = { onEvent(BlockEvent.HideSessionDatePickerDialog) }
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BlockTopAppBar(
    state: BlockUiState,
    onEvent: (BlockEvent) -> Unit,
    topAppBarScrollBehavior: TopAppBarScrollBehavior
) {
    MediumTopAppBar(
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
private fun DayNavigationBottomBar(
    state: BlockUiState,
    onEvent: (BlockEvent) -> Unit
) {
    if (state.days.size <= 1) return

    NavigationBar {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(state.days) { day ->
                Text(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable { onEvent(BlockEvent.SelectDay(day.toDay())) }
                        .background(
                            if (day.toDay() == state.selectedDay) {
                                MaterialTheme.colorScheme.primary
                            } else Color.Transparent
                        )
                        .padding(4.dp)
                        .widthIn(64.dp, 128.dp),
                    text = day.name,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}