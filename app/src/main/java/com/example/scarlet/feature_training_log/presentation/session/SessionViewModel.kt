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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
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
    val state = combine(
        _state,
        useCases.getExercisesWithMovementAndSetsBySessionId(session.id)
    ) { state, exercises ->
        state.copy(
            exercises = exercises.data ?: emptyList()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SessionUiState(session = session))

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
                    useCases.updateSession(_state.value.session)
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
            SessionEvent.ToggleMovementSelectionSheet -> {
                _state.update {
                    it.copy(
                        isMovementSelectionSheetOpen = !it.isMovementSelectionSheetOpen
                    ).also { updatedState ->
                        if(updatedState.isMovementSelectionSheetOpen) {
                            updateMovementNameFilter("")
                        }
                    }
                }
            }
            is SessionEvent.FilterMovementsByName -> {
                updateMovementNameFilter(event.nameFilter)
            }
            SessionEvent.ToggleNewMovementSheet -> {
                _state.update { it.copy(
                    isNewMovementSheetOpen = !it.isNewMovementSheetOpen
                )}
            }
            is SessionEvent.AddMovement -> {
                viewModelScope.launch(Dispatchers.IO) {
                    when (val insertedMovementIdResource = useCases.insertMovement(event.name)) {
                        is Resource.Success -> {
                            useCases.insertExercise(
                                Exercise(
                                    sessionId = _state.value.session.id,
                                    movementId = insertedMovementIdResource.data!!.toInt(),
                                    order = state.value.exercises.size + 1
                                )
                            )
                            _state.update {
                                it.copy(
                                    isNewMovementSheetOpen = false,
                                    isMovementSelectionSheetOpen = false
                                )
                            }
                        }
                        else -> {
                            /* TODO */
                        }
                    }
                }
            }
            is SessionEvent.AddExercise -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.insertExercise(
                        Exercise(
                            sessionId = _state.value.session.id,
                            movementId = event.movementId,
                            order = state.value.exercises.size + 1
                        )
                    )
                }
                _state.update { it.copy(
                    isMovementSelectionSheetOpen = false
                )}
            }
            is SessionEvent.DeleteExercise -> {
                /* TODO */
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
        }
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