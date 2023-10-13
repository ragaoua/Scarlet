package com.example.scarlet.feature_training_log.presentation.block

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.R
import com.example.scarlet.core.util.StringResource
import com.example.scarlet.core.util.roundToClosestMultipleOf
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercises
import com.example.scarlet.feature_training_log.presentation.destinations.BlockScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockViewModel @Inject constructor(
    private val useCases : BlockUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var filterMovementsJob: Job? = null

    private val _uiActions = Channel<UiAction>()
    val uiActions = _uiActions.receiveAsFlow()

    private val _state = MutableStateFlow(BlockUiState(
        block = BlockScreenDestination.argsFrom(savedStateHandle).block
    ))
    val state = _state.asStateFlow()

    private var updateCalculatedLoadJob: Job? = null
    private val LOAD_CALCULATION_DELAY = 500L

    init {
        initDataCollection()
    }

    fun onEvent(event: BlockEvent) {
        when(event) {
            BlockEvent.AddSession -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.insertSession(state.value.selectedDayId)
                }
            }
            is BlockEvent.DeleteSession -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val sessionToRestore = SessionWithExercises(
                        session = event.session,
                        exercises = useCases.getExercisesWithMovementAndSetsBySessionId(
                            event.session.id
                        ).data ?: emptyList()
                    )

                    useCases.deleteSession(event.session)

                    _uiActions.send(UiAction.ShowSnackbar(
                        message = StringResource(R.string.session_deleted),
                        actionLabel = StringResource(R.string.undo),
                        onActionPerformed = {
                            viewModelScope.launch(Dispatchers.IO) {
                                useCases.restoreSessionWithExercisesWithMovementAndSets(sessionToRestore)
                            }
                        }
                    ))
                }
            }
            is BlockEvent.UpdateSessionIndexScrollPosition -> {
                _state.update { it.copy(
                    sessionIndexScrollPositionByDayId = it.sessionIndexScrollPositionByDayId
                        .toMutableMap()
                        .apply { put(it.selectedDayId, event.index) }
                )}
            }
            BlockEvent.EditBlock -> {
                _state.update { it.copy(
                    editBlockSheet = BlockUiState.EditBlockSheetState(
                        blockName = state.value.block.name
                    )
                )}
            }
            BlockEvent.CancelBlockEdition -> {
                _state.update { it.copy(
                    editBlockSheet = null
                )}
            }
            is BlockEvent.UpdateEditedBlockName -> {
                _state.update { it.copy(
                    editBlockSheet = it.editBlockSheet?.copy(
                        blockName = event.editedBlockName
                    )
                )}
            }
            is BlockEvent.SaveBlockName -> {
                viewModelScope.launch(Dispatchers.IO) {
                    state.value.editBlockSheet?.blockName?.let { editedBlockName ->
                        if(editedBlockName == state.value.block.name) {
                            _state.update { it.copy(
                                editBlockSheet = null
                            )}
                            return@launch
                        }

                        val updatedBlock = state.value.block.copy(
                            name = editedBlockName
                        )
                        useCases.updateBlock(updatedBlock).also { resource ->
                            resource.error?.let { error ->
                                _state.update { it.copy (
                                    editBlockSheet = it.editBlockSheet?.copy(
                                        blockNameError = error
                                    )
                                )}
                            } ?: run {
                                _state.update { it.copy(
                                    block = updatedBlock,
                                    editBlockSheet = null
                                )}
                            }
                        }
                    }
                }
            }
            is BlockEvent.SelectDay -> {
                _state.update { it.copy(
                    selectedDayId = event.day.id
                )}
            }
            is BlockEvent.ShowSessionDatePickerDialog -> {
                _state.update { it.copy(
                    sessionDatePickerDialog = BlockUiState.SessionDatePickerDialogState(
                        session = event.session
                    )
                )}
            }
            BlockEvent.HideSessionDatePickerDialog -> {
                _state.update { it.copy(
                    sessionDatePickerDialog = null
                )}
            }
            is BlockEvent.UpdateSessionDate -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val updatedSession = state.value.sessionDatePickerDialog?.session?.copy(
                        date = event.date
                    ) ?: return@launch
                    useCases.updateSession(updatedSession)

                    _state.update { it.copy(
                        sessionDatePickerDialog = null
                    )}
                }
            }
            BlockEvent.ToggleSessionEditMode -> {
                _state.update { it.copy(
                    isInSessionEditMode = !it.isInSessionEditMode
                )}
            }
            is BlockEvent.ToggleExerciseDetail -> {
                _state.update { it.copy(
                    isExerciseDetailExpandedById = it.isExerciseDetailExpandedById[event.exerciseId]
                        ?.let { isExpanded ->
                            it.isExerciseDetailExpandedById
                                .toMutableMap()
                                .apply { put(event.exerciseId, !isExpanded) }
                        } ?: return
                )}
            }
            is BlockEvent.ToggleSessionExercisesDetails -> {
                _state.update { state ->
                    val sessionExercisesIds = state.days
                        .flatMap { it.sessions }
                        .find { it.id == event.sessionId }
                        ?.exercises
                        ?.map { it.id }
                        ?: return
                    val isAtLeastOneExerciseExpanded = state.isExerciseDetailExpandedById
                        .filter { it.key in sessionExercisesIds }
                        .any { it.value }
                    state.copy(
                    isExerciseDetailExpandedById = state.isExerciseDetailExpandedById
                        .map {
                            Pair(
                                it.key,
                                if(it.key in sessionExercisesIds) {
                                    !isAtLeastOneExerciseExpanded
                                } else it.value
                            )
                        }.toMap()
                )}
            }
            is BlockEvent.ShowExerciseDropdownMenu -> {
                _state.update { it.copy(
                    expandedDropdownMenuExerciseId = event.exerciseId
                )}
            }
            BlockEvent.DismissExerciseDropdownMenu -> {
                _state.update { it.copy(
                    expandedDropdownMenuExerciseId = null
                )}
            }
            is BlockEvent.ShowMovementSelectionSheet -> {
                _state.update { it.copy(
                    movementSelectionSheet = BlockUiState.MovementSelectionSheetState(
                        session = event.session,
                        exercise = event.exercise
                    ),
                    expandedDropdownMenuExerciseId = null
                )}
                updateMovementNameFilter("")
            }
            BlockEvent.HideMovementSelectionSheet -> {
                _state.update { it.copy(
                    movementSelectionSheet = null
                )}
            }
            is BlockEvent.FilterMovementsByName -> {
                _state.update { it.copy(
                    movementSelectionSheet = it.movementSelectionSheet?.copy(
                        movementNameFilter = event.nameFilter
                    )
                )}
                updateMovementNameFilter(event.nameFilter)
            }
            is BlockEvent.AddMovement -> {
                state.value.movementSelectionSheet?.let { sheet ->
                    viewModelScope.launch(Dispatchers.IO) {
                        useCases.insertMovement(sheet.movementNameFilter).also { resource ->
                            resource.error?.let {
                                _uiActions.send(UiAction.ShowSnackbar(it))
                            }
                        }
                    }
                }
            }
            is BlockEvent.ShowEditMovementSheet -> {
                _state.update { state -> state.copy(
                    movementSelectionSheet = state.movementSelectionSheet?.copy(
                        editMovementSheet = BlockUiState.EditMovementSheetState(
                            movement = event.movement
                        )
                    )
                )}
            }
            BlockEvent.HideEditMovementSheet -> {
                _state.update { state -> state.copy(
                    movementSelectionSheet = state.movementSelectionSheet?.copy(
                        editMovementSheet = null
                    )
                )}
            }
            is BlockEvent.UpdateEditedMovementName -> {
                _state.update { state -> state.copy(
                    movementSelectionSheet = state.movementSelectionSheet?.let { it.copy(
                            editMovementSheet = it.editMovementSheet?.copy(
                                editedMovementName = event.editedMovementName
                            )
                        )
                    }
                )}
            }
            BlockEvent.UpdateEditedMovement -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val sheet = state.value.movementSelectionSheet?.editMovementSheet
                        ?: return@launch

                    if(sheet.editedMovementName == sheet.movement.name) {
                        _state.update { state -> state.copy(
                            movementSelectionSheet = state.movementSelectionSheet?.copy(
                                editMovementSheet = null
                            )
                        )}
                        return@launch
                    }

                    useCases.updateMovement(
                        sheet.movement.copy(name = sheet.editedMovementName)
                    ).also { resource ->
                        resource.error?.let { error ->
                            _state.update { it.copy(
                                movementSelectionSheet = it.movementSelectionSheet?.copy(
                                    editMovementSheet = sheet.copy(
                                        movementNameError = error
                                    )
                                )
                            )}
                        } ?: run {
                            _state.update { state -> state.copy(
                                movementSelectionSheet = state.movementSelectionSheet?.copy(
                                    editMovementSheet = null
                                )
                            )}
                        }
                    }
                }
            }
            BlockEvent.DeleteEditedMovement -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.deleteMovement(
                        movement = state.value.movementSelectionSheet?.editMovementSheet?.movement
                            ?: return@launch
                    )
                    _state.update { it.copy(
                        movementSelectionSheet = it.movementSelectionSheet?.copy(
                            editMovementSheet = null
                        )
                    )}
                }
            }
            is BlockEvent.SelectMovement -> {
                state.value.movementSelectionSheet?.let { sheet ->
                    if (sheet.isInSupersetSelectionMode) {
                        var keepSupersetSelectionMode: Boolean
                        _state.update { state -> state.copy(
                            movementSelectionSheet = sheet.copy(
                                supersetMovements = sheet.supersetMovements
                                    .toMutableList()
                                    .apply {
                                        if (event.movement in this) {
                                            remove(event.movement)
                                        } else add(event.movement)
                                    }.also {
                                        keepSupersetSelectionMode = it.isNotEmpty()
                                    },
                                isInSupersetSelectionMode = keepSupersetSelectionMode
                            )
                        )}
                    } else {
                        viewModelScope.launch(Dispatchers.IO) {
                            sheet.exercise?.let { exercise ->
                                useCases.updateExercise(
                                    exercise = exercise.copy(
                                        movementId = event.movement.id
                                    )
                                )
                            } ?: run {
                                useCases.insertExercise(
                                    sessionId = sheet.session.id,
                                    movementId = event.movement.id
                                )
                            }
                        }
                        _state.update { state -> state.copy(
                            movementSelectionSheet = null
                        )}
                    }
                }
            }
            is BlockEvent.EnableSupersetSelectionMode -> {
                state.value.movementSelectionSheet?.let { sheet ->
                    _state.update { state -> state.copy(
                        movementSelectionSheet = sheet.copy(
                            isInSupersetSelectionMode = true,
                            supersetMovements = listOf(event.movement)
                        )
                    )}
                }
            }
            is BlockEvent.DisableSupersetSelectionMode -> {
                _state.update { state -> state.copy(
                    movementSelectionSheet = state.movementSelectionSheet?.copy(
                        isInSupersetSelectionMode = false,
                        supersetMovements = emptyList()
                    )
                )}
            }
            is BlockEvent.AddSuperset -> {
                state.value.movementSelectionSheet?.let { sheet ->
                    viewModelScope.launch(Dispatchers.IO) {
                        useCases.insertSuperset(
                            sessionId = sheet.session.id,
                            movementIds = sheet.supersetMovements.map { it.id }
                        )
                    }
                    _state.update { state -> state.copy(
                        movementSelectionSheet = null
                    )}
                }
            }
            is BlockEvent.DeleteExercise -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val isExerciseInASuperset = state.value.days
                        .flatMap { it.sessions }
                        .flatMap { it.exercises }
                        .any { it.order == event.exercise.order &&
                                it.id != event.exercise.id }

                    useCases.deleteExercise(exercise = event.exercise.toExercise())

                    _uiActions.send(UiAction.ShowSnackbar(
                        message = StringResource(R.string.exercise_deleted),
                        actionLabel = StringResource(R.string.undo),
                        onActionPerformed = {
                            viewModelScope.launch(Dispatchers.IO) {
                                useCases.restoreExerciseWithMovementAndSets(
                                    exercise = event.exercise,
                                    isExerciseInASuperset = isExerciseInASuperset
                                )
                            }
                        }
                    ))
                }
                _state.update { state -> state.copy(
                    expandedDropdownMenuExerciseId = null
                )}
            }
            is BlockEvent.UpdateRatingType -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.updateExercise(
                        exercise = event.exercise.copy(
                            ratingType = event.ratingType
                        )
                    )
                }
            }
            is BlockEvent.AddSet -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.insertEmptySetWhileSettingsOrder(event.exercise.id)
                }
            }
            is BlockEvent.UpdateSet -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.updateSet(event.set)
                }
            }
            is BlockEvent.DeleteSet -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.deleteSet(event.set)

                    _uiActions.send(UiAction.ShowSnackbar(
                        message = StringResource(R.string.set_deleted),
                        actionLabel = StringResource(R.string.undo),
                        onActionPerformed = {
                            viewModelScope.launch(Dispatchers.IO) {
                                useCases.restoreSet(event.set)
                            }
                        }
                    ))
                }
            }
            is BlockEvent.CopyPreviousSet -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.copyPrecedingSetField(
                        set = event.set,
                        sessionExercises = state.value.days
                            .flatMap { it.sessions }
                            .flatMap { it.exercises },
                        fieldToCopy = event.fieldToCopy
                    )
                }
            }
            is BlockEvent.ShowLoadCalculationDialog -> {
                val previousSetLoad = state.value.days
                    .flatMap { it.sessions }
                    .flatMap { it.exercises }
                    .flatMap { it.sets }
                    .find { it.exerciseId == event.set.exerciseId &&
                            it.order == event.set.order - 1
                    }?.load
                    ?: return
                _state.update { it.copy(
                    loadCalculationDialog = BlockUiState.LoadCalculationDialogState(
                        set = event.set,
                        previousSetLoad = previousSetLoad
                    )
                )}
            }
            is BlockEvent.HideLoadCalculationDialog -> {
                _state.update { it.copy(
                    loadCalculationDialog = null
                )}
            }
            is BlockEvent.UpdateLoadPercentage -> {
                val percentage = event.percentage.toIntOrNull()
                    ?: if(event.percentage.isBlank()) null else return

                if (percentage != null && percentage !in 0..100) return


                _state.update { it.copy(
                    loadCalculationDialog = it.loadCalculationDialog?.copy(
                        percentage = percentage
                    )
                )}

                updateCalculatedLoadJob?.cancel()
                updateCalculatedLoadJob = viewModelScope.launch {
                    val dialog = state.value.loadCalculationDialog ?: return@launch

                    delay(LOAD_CALCULATION_DELAY)
                    _state.update { state -> state.copy(
                        loadCalculationDialog = state.loadCalculationDialog?.copy(
                            calculatedLoad = percentage?.let {
                                (it * dialog.previousSetLoad / 100)
                                    .roundToClosestMultipleOf(0.5f)
                            }
                        )
                    )}
                }
            }
            is BlockEvent.UpdateCalculatedLoad -> {
                _state.update { it.copy(
                    loadCalculationDialog = it.loadCalculationDialog?.copy(
                        calculatedLoad = event.load
                    )
                )}
            }
            BlockEvent.UpdateSetBasedOnPrecedingSet -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.updateSet(
                        set = state.value.loadCalculationDialog?.set?.copy(
                            load = state.value.loadCalculationDialog?.calculatedLoad
                        ) ?: return@launch
                    )
                    _state.update { it.copy(
                        loadCalculationDialog = null
                    )}
                }
            }
            BlockEvent.ShowFloatingActionButtons -> {
                _state.update { it.copy(
                    areFloatingActionButtonsVisible = true
                )}
            }
            BlockEvent.HideFloatingActionButtons -> {
                _state.update { it.copy(
                    areFloatingActionButtonsVisible = false
                )}
            }
        }
    }

    private fun initDataCollection() {
        useCases.getDaysWithSessionsWithExercisesWithMovementAndSetsByBlockId(state.value.block.id)
            .onEach { resource ->
                val days = resource.data ?: emptyList()
                _state.update { state -> state.copy(
                    days = days,
                    isInSessionEditMode = if (days.flatMap { it.sessions }.isNotEmpty()) {
                        state.isInSessionEditMode
                    } else false,
                    isExerciseDetailExpandedById = days.flatMap { it.sessions }
                        .flatMap { it.exercises }
                        .associate { day -> Pair(
                            day.id,
                            state.isExerciseDetailExpandedById[day.id] ?: true
                        ) },
                    selectedDayId =
                        if (state.selectedDayId in days.map { it.id }) {
                            state.selectedDayId
                        } else {
                            val daysWithSessions = days.filter { it.sessions.isNotEmpty() }
                            if (daysWithSessions.isNotEmpty()) {
                                daysWithSessions.maxBy { day ->
                                    day.sessions.maxOf { it.date }
                                }.id
                            } else days.firstOrNull()?.id ?: 0
                        },
                    sessionIndexScrollPositionByDayId = days.associate { day ->
                        val oldSessions = state.days.find { it.id == day.id }?.sessions ?: emptyList()

                        // if sessions have been added, scroll to the latest one
                        val latestAddedSession =
                            day.sessions.minus(oldSessions.toSet()).maxWithOrNull(
                                compareBy ({ it.date }, { it.id })
                            )

                        Pair(
                            day.id,
                            latestAddedSession?.let { day.sessions.indexOf(it) }
                                ?: state.sessionIndexScrollPositionByDayId[day.id]
                                ?: day.sessions.lastIndex.let { if(it == -1) 0 else it }
                        )
                    }
                )}
            }.launchIn(viewModelScope)
    }

    private fun updateMovementNameFilter(nameFilter: String) {
        state.value.movementSelectionSheet?.let { sheet ->
            filterMovementsJob?.cancel()
            filterMovementsJob = useCases.getMovementsFilteredByName(nameFilter)
                .onEach { movements ->
                    _state.update { state -> state.copy(
                        movementSelectionSheet = sheet.copy(
                            movements = movements.data ?: emptyList()
                        )
                    )}
                }.launchIn(viewModelScope)
        }
    }

    sealed interface UiAction {
        object NavigateUp: UiAction
        class ShowSnackbar(
            val message: StringResource,
            val actionLabel: StringResource? = null,
            val onActionPerformed: (() -> Unit)? = null
        ): UiAction
    }
}