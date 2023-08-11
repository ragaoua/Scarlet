package com.example.scarlet.feature_training_log.presentation.session

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.use_case.session.SessionUseCases
import com.example.scarlet.feature_training_log.presentation.destinations.SessionScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val useCases: SessionUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var filterMovementsJob: Job? = null

    private val _state = MutableStateFlow(
        SessionUiState(
            session = SessionScreenDestination.argsFrom(savedStateHandle).session,
            sessionBlockName = SessionScreenDestination.argsFrom(savedStateHandle).block.name
        )
    )
    val state = _state.asStateFlow()

    init {
        initSessionExercisesCollection()
    }

    fun onEvent(event: SessionEvent) {
        when (event) {
            SessionEvent.ToggleDatePickerDialog -> {
                _state.update { it.copy(
                    isDatePickerDialogOpen = !it.isDatePickerDialogOpen
                )}
            }
            is SessionEvent.UpdateSessionDate -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.updateSession(state.value.session)
                    _state.update { it.copy(
                        session = it.session.copy (
                            date = event.date
                        ),
                        isDatePickerDialogOpen = false
                    )}
                }
            }
            SessionEvent.ToggleEditMode -> {
                _state.update { it.copy(
                    isInEditMode = !it.isInEditMode
                )}
            }
            is SessionEvent.ShowMovementSelectionSheet -> {
                updateMovementNameFilter("")
                _state.update { it.copy(
                    isMovementSelectionSheetOpen = true,
                    exerciseToEdit = event.exercise
                )}
            }
            SessionEvent.HideMovementSelectionSheet -> {
                _state.update { it.copy(
                    isMovementSelectionSheetOpen = false,
                    exerciseToEdit = null
                )}
            }
            is SessionEvent.FilterMovementsByName -> {
                updateMovementNameFilter(event.nameFilter)
            }
            is SessionEvent.AddMovement -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.insertMovement(event.name).also { resource ->
                        resource.error?.let {
                            /* TODO */
                        }
                        resource.data?.let { insertedMovementId ->
                            state.value.exerciseToEdit?.let { exercise ->
                                useCases.updateExercise(
                                    exercise = exercise.copy(
                                        movementId = insertedMovementId,
                                    )
                                )
                            } ?: run {
                                useCases.insertExercise(
                                    Exercise(
                                        sessionId = state.value.session.id,
                                        movementId = insertedMovementId,
                                        order = state.value.exercises.size + 1
                                    )
                                )
                            }
                            _state.update {
                                it.copy(
                                    isMovementSelectionSheetOpen = false,
                                    exerciseToEdit = null
                                )
                            }
                        }
                    }
                }
            }
            is SessionEvent.SelectMovement -> {
                viewModelScope.launch(Dispatchers.IO) {
                    state.value.exerciseToEdit?.let { exercise ->
                        useCases.updateExercise(
                            exercise = exercise.copy(
                                movementId = event.movementId
                            )
                        )
                    } ?: run {
                        useCases.insertExercise(
                            Exercise(
                                sessionId = state.value.session.id,
                                movementId = event.movementId,
                                order = state.value.exercises.size + 1
                            )
                        )
                    }
                    _state.update { it.copy(
                        isMovementSelectionSheetOpen = false,
                        exerciseToEdit = null
                    )}
                }
            }
            is SessionEvent.DeleteExercise -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val sessionExercises = state.value.exercises.map { it.exercise }
                    useCases.deleteExercise(
                        exercise = event.exercise,
                        sessionExercises = sessionExercises
                    )
                }
            }
            is SessionEvent.AddSet -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val exerciseSets = state.value.exercises
                        .find { it.exercise.id == event.exercise.id }
                        ?.sets ?: emptyList()
                    useCases.insertSet(
                        exerciseId = event.exercise.id,
                        exerciseSets = exerciseSets
                    )
                }
            }
            is SessionEvent.UpdateSet -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.updateSet(event.set)
                }
            }
            is SessionEvent.DeleteSet -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.deleteSet(
                        set = event.set,
                        exerciseSets = state.value.exercises
                            .find { it.exercise.id == event.set.exerciseId }
                            ?.sets ?: emptyList()
                    )
                }
            }
            is SessionEvent.CopyPreviousSet -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.copyPrecedingSetField(
                        set = event.set,
                        sessionExercises = state.value.exercises,
                        fieldToCopy = event.fieldToCopy
                    )
                }
            }
            is SessionEvent.ShowLoadCalculationDialog -> {
                val previousSet = state.value.exercises
                    .find { it.exercise.id == event.set.exerciseId }
                    ?.sets
                    ?.find { it.order == event.set.order - 1 }
                    ?: run {
                        /* TODO Send an error to the UI */
                        return
                    }
                _state.update { it.copy(
                    loadCalculationDialogState = SessionUiState.LoadCalculationDialogState(
                        set = event.set,
                        previousSet = previousSet
                    )
                )}
            }
            is SessionEvent.CalculateLoad -> {
                viewModelScope.launch(Dispatchers.IO) {
                    state.value.loadCalculationDialogState?.let { dialog ->
                        useCases.updateLoadBasedOnPreviousSet(
                            set = dialog.set,
                            precedingSet = dialog.previousSet,
                            loadPercentage = event.percentage
                        )
                    } ?: run {
                        /* TODO display an error */
                    }
                    _state.update { it.copy(
                        loadCalculationDialogState = null
                    )}
                }
            }
            is SessionEvent.HideLoadCalculationDialog -> {
                _state.update { it.copy(
                    loadCalculationDialogState = null
                )}
            }
        }
    }

    private fun initSessionExercisesCollection() {
        useCases.getExercisesWithMovementAndSetsBySessionId(state.value.session.id)
            .onEach { exercises ->
                _state.update { it.copy(
                    exercises = exercises.data ?: emptyList()
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
}