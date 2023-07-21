package com.example.scarlet.feature_training_log.presentation.session

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.use_case.session.SessionUseCases
import com.example.scarlet.feature_training_log.presentation.destinations.SessionScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
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

    private val _state = MutableStateFlow(
        SessionUiState(
            session = session,
            sessionBlockName = sessionBlock.name
        )
    )
    val state = combine(
        _state,
        useCases.getExercisesWithMovementAndSetsBySessionId(session.id),
        useCases.getAllMovements()
    ) { state, exercises, movements ->
        state.copy(
            exercises = exercises.data ?: emptyList(),
            movements = movements.data?.filter {
                it.name.contains(
                    other = state.movementNameFilter,
                    ignoreCase = true
                ) // TODO apply the filter in the use case
            } ?: emptyList()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SessionUiState(session = session))

    fun onEvent(event: SessionEvent) {
        when (event) {
            SessionEvent.OpenDatePickerDialog -> {
                _state.update { it.copy(
                    isDatePickerDialogOpen = true
                )}
            }
            SessionEvent.CloseDatePickerDialog -> {
                _state.update { it.copy(
                    isDatePickerDialogOpen = false
                )}
            }
            SessionEvent.ToggleEditMode -> {
                _state.update { it.copy(
                    isInEditMode = !it.isInEditMode
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
            SessionEvent.ExpandMovementSelectionSheet -> {
                _state.update { it.copy(
                    isMovementSelectionSheetOpen = true
                )}
            }
            SessionEvent.CollapseMovementSelectionSheet -> {
                _state.update { it.copy(
                    isMovementSelectionSheetOpen = false
                )}
            }
            is SessionEvent.NewExercise -> {
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
            is SessionEvent.NewSet -> {
                viewModelScope.launch(Dispatchers.IO) {
                    // TODO find a way to define the order of the set in the use case
                    val newSetOrder = state.value.exercises
                        .flatMap { it.sets }
                        .count { it.exerciseId == event.exercise.id } + 1
                    val newSet = Set(
                        exerciseId = event.exercise.id,
                        order = newSetOrder
                    )
                    useCases.insertSet(newSet)
                }
            }
            is SessionEvent.UpdateSet -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.updateSet(event.set)
                }
            }
            is SessionEvent.DeleteSet -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.deleteSet(event.set)
                    /* Update the order of the other sets if necessary */
                    // TODO find a way to do that in the use case
                    state.value.exercises
                        .flatMap { it.sets }
                        .filter {
                            it.exerciseId == event.set.exerciseId && it.order > event.set.order
                        }.forEach {
                            useCases.updateSet(it.copy(order = it.order - 1))
                        }
                }
            }
            is SessionEvent.FilterMovementsByName -> {
                _state.update { it.copy(
                    movementNameFilter = event.nameFilter
                )}
            }
        }
    }
}