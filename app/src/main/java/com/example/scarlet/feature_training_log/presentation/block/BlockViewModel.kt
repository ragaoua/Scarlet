package com.example.scarlet.feature_training_log.presentation.block

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.feature_training_log.domain.model.Exercise
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

    private var updateLoadSuggestionsJob: Job? = null
    private val LOAD_SUGGESTIONS_DELAY = 500L

    init {
        initBlockSessionsCollection()
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
                    useCases.deleteSession(event.session)
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
            is BlockEvent.ShowMovementSelectionSheet -> {
                updateMovementNameFilter("")
                _state.update { it.copy(
                    movementSelectionSheet = BlockUiState.MovementSelectionSheetState(
                        session = event.session,
                        exercise = event.exercise
                    )
                )}
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
                viewModelScope.launch(Dispatchers.IO) {
                    state.value.movementSelectionSheet?.let { sheet ->
                        useCases.insertMovement(sheet.movementNameFilter).also { resource ->
                            resource.error?.let {
                                /* TODO */
                            }
                            resource.data?.let { insertedMovementId ->
                                sheet.exercise?.let { exercise ->
                                    useCases.updateExercise(
                                        exercise = exercise.copy(
                                            movementId = insertedMovementId,
                                        )
                                    )
                                } ?: run {
                                    useCases.insertExercise(
                                        Exercise(
                                            sessionId = sheet.session.id,
                                            movementId = insertedMovementId,
                                            order = sheet.session.exercises.size + 1
                                        )
                                    )
                                }
                                _state.update { it.copy(
                                    movementSelectionSheet = null
                                )}
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
                viewModelScope.launch(Dispatchers.IO) {
                    state.value.movementSelectionSheet?.exercise?.let { exercise ->
                        useCases.updateExercise(
                            exercise = exercise.copy(
                                movementId = event.movement.id
                            )
                        )
                    } ?: run {
                        state.value.movementSelectionSheet?.let { sheet ->
                            useCases.insertExercise(
                                Exercise(
                                    sessionId = sheet.session.id,
                                    movementId = event.movement.id,
                                    order = sheet.session.exercises.size + 1
                                )
                            )
                        }
                    }
                    _state.update { it.copy(
                        movementSelectionSheet = null
                    )}
                }
            }
            is BlockEvent.DeleteExercise -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.deleteExercise(
                        exercise = event.exercise,
                        sessionExercises = state.value.days
                            .flatMap { it.sessions }
                            .flatMap { it.exercises }
                            .filter { it.sessionId == event.exercise.sessionId }
                            .map { it.toExercise() }
                    )
                }
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
                    useCases.insertSet(
                        exerciseId = event.exercise.id,
                        exerciseSets = state.value.days
                            .flatMap { it.sessions }
                            .flatMap { it.exercises }
                            .flatMap { it.sets }
                            .filter { it.exerciseId == event.exercise.id }
                    )
                }
            }
            is BlockEvent.UpdateSet -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.updateSet(event.set)
                }
            }
            is BlockEvent.DeleteSet -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.deleteSet(
                        set = event.set,
                        exerciseSets = state.value.days
                            .flatMap { it.sessions }
                            .flatMap { it.exercises }
                            .flatMap { it.sets }
                            .filter { it.exerciseId == event.set.exerciseId }
                    )
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
                    }?.weight
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
                if (percentage != null && percentage !in 1..100) return

                _state.update { it.copy(
                    loadCalculationDialog = it.loadCalculationDialog?.copy(
                        percentage = percentage
                    )
                )}

                updateLoadSuggestionsJob?.cancel()
                updateLoadSuggestionsJob = viewModelScope.launch {
                    val dialog = state.value.loadCalculationDialog ?: return@launch

                    delay(LOAD_SUGGESTIONS_DELAY)
                    _state.update { state -> state.copy(
                        loadCalculationDialog = state.loadCalculationDialog?.copy(
                            calculatedLoad = percentage?.let {
                                it * dialog.previousSetLoad / 100
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
                            weight = state.value.loadCalculationDialog?.calculatedLoad
                        ) ?: return@launch
                    )
                    _state.update { it.copy(
                        loadCalculationDialog = null
                    )}
                }
            }
        }
    }

    private fun initBlockSessionsCollection() {
        useCases.getDaysWithSessionsWithMovementAndSetsByBlockId(state.value.block.id)
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
                        } else days.firstOrNull()?.id ?: 0,
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
        filterMovementsJob?.cancel()
        filterMovementsJob = useCases.getMovementsFilteredByName(nameFilter)
            .onEach { movements ->
                _state.update { it.copy(
                    movements = movements.data ?: emptyList()
                )}
            }.launchIn(viewModelScope)
    }

    sealed interface UiAction {
        object NavigateUp: UiAction
    }
}