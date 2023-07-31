package com.example.scarlet.feature_training_log.presentation.session

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.core.util.Resource
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

    private val session = SessionScreenDestination.argsFrom(savedStateHandle).session
    private val sessionBlock = SessionScreenDestination.argsFrom(savedStateHandle).block

    private var filterMovementsJob: Job? = null

    private val _state = MutableStateFlow(
        SessionUiState(
            session = session,
            sessionBlockName = sessionBlock.name
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
                    _state.update { it.copy(
                        session = it.session.copy (
                            date = event.date
                        )
                    )}
                    useCases.updateSession(state.value.session)
                    _state.update { it.copy(
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
                    val insertedMovementIdResource = useCases.insertMovement(event.name)
                    when (insertedMovementIdResource) {
                        is Resource.Success -> {
                            state.value.exerciseToEdit?.let { exercise ->
                                useCases.updateExercise(
                                    exercise = exercise.copy(
                                        movementId = insertedMovementIdResource.data!!.toInt(),
                                    )
                                )
                            } ?: run {
                                useCases.insertExercise(
                                    Exercise(
                                        sessionId = state.value.session.id,
                                        movementId = insertedMovementIdResource.data!!.toInt(),
                                        order = state.value.exercises.size + 1
                                    )
                                )
                            }
                            _state.update { it.copy(
                                isMovementSelectionSheetOpen = false,
                                exerciseToEdit = null
                            )}
                        } else -> {
                            /* TODO */
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
                    useCases.copyPreviousSetField(
                        set = event.set,
                        sessionExercises = state.value.exercises,
                        fieldToCopy = event.fieldToCopy
                    )
                }
            }
        }
    }

    private fun initSessionExercisesCollection() {
        useCases.getExercisesWithMovementAndSetsBySessionId(session.id)
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