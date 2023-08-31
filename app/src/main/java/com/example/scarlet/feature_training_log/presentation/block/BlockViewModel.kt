package com.example.scarlet.feature_training_log.presentation.block

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.use_case.block.BlockUseCases
import com.example.scarlet.feature_training_log.presentation.destinations.BlockScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
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
            BlockEvent.EditBlock -> {
                _state.update { it.copy(
                    editBlockSheet = BlockUiState.EditBlockSheetState(
                        blockName = state.value.block.name,
                        areMicroCycleSettingsExpanded = false, // TODO
                        daysPerMicroCycle = 3 // TODO
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
            BlockEvent.ToggleMicroCycleSettings -> {
                _state.update { it.copy(
                    editBlockSheet = it.editBlockSheet?.copy(
                        areMicroCycleSettingsExpanded = !it.editBlockSheet.areMicroCycleSettingsExpanded
                    )
                )}
            }
            is BlockEvent.UpdateDaysPerMicroCycle -> {
                _state.update { it.copy(
                    editBlockSheet = it.editBlockSheet?.copy(
                        daysPerMicroCycle = event.nbDays
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
                val daySessions = state.value.days
                    .find { day -> day.toDay() == event.day }
                    ?.sessions ?: return
                _state.update { it.copy(
                    selectedDayId = event.day.id,
                    visibleSessionIndex =
                        if(daySessions.isNotEmpty()) {
                            daySessions.lastIndex
                        } else 0
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
                val previousSet = state.value.days
                    .flatMap { it.sessions }
                    .flatMap { it.exercises }
                    .flatMap { it.sets }
                    .find { it.exerciseId == event.set.exerciseId &&
                            it.order == event.set.order - 1
                    } ?: return
                _state.update { it.copy(
                    loadCalculationDialog = BlockUiState.LoadCalculationDialogState(
                        set = event.set,
                        previousSet = previousSet
                    )
                )}
            }
            is BlockEvent.HideLoadCalculationDialog -> {
                _state.update { it.copy(
                    loadCalculationDialog = null
                )}
            }
            is BlockEvent.CalculateLoad -> {
                viewModelScope.launch(Dispatchers.IO) {
                    state.value.loadCalculationDialog?.let { dialog ->
                        useCases.updateLoadBasedOnPreviousSet(
                            set = dialog.set,
                            precedingSet = dialog.previousSet,
                            loadPercentage = event.percentage
                        )
                    }
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
                _state.update { state ->

                    val selectedDayId =
                        if (state.selectedDayId in days.map { it.id }) {
                            state.selectedDayId
                        } else days.firstOrNull()?.id ?: 0

                    val selectedDaySessions = days
                        .find { it.id == selectedDayId }
                        ?.sessions ?: emptyList()

                    state.copy(
                        days = days,
                        selectedDayId = selectedDayId,
                        visibleSessionIndex =
                            if(selectedDaySessions.isNotEmpty()) {
                                selectedDaySessions.lastIndex
                            } else 0
                    )
                }
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