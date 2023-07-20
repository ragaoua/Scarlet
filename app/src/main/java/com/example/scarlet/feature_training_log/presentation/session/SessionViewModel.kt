package com.example.scarlet.feature_training_log.presentation.session

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.feature_training_log.data.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.Set
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
    private val repository: ScarletRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val session = SessionScreenDestination.argsFrom(savedStateHandle).session
    private val sessionBlock = SessionScreenDestination.argsFrom(savedStateHandle).block
    private val exercises = repository.getExercisesWithMovementAndSetsBySessionId(session.id)
    private val movements = repository.getAllMovements()

    private val _state = MutableStateFlow(
        SessionUiState(
            session = session,
            sessionBlockName = sessionBlock.name
        )
    )
    val state = combine(_state, exercises, movements) { state, exercises, movements ->
        state.copy(
            exercises = exercises.sortedBy { it.exercise.order },
            movements = movements.filter {
                it.name.contains(
                    other = state.movementNameFilter,
                    ignoreCase = true
                )
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SessionUiState(session = session)
    )

    fun onEvent(event: SessionEvent) {
        when (event) {
            SessionEvent.OpenDatePickerDialog -> {
                _state.update {
                    it.copy(isDatePickerDialogOpen = true)
                }
            }
            SessionEvent.CloseDatePickerDialog -> {
                _state.update {
                    it.copy(isDatePickerDialogOpen = false)
                }
            }
            SessionEvent.ToggleEditMode -> {
                _state.update {
                    it.copy(isInEditMode = !it.isInEditMode)
                }
            }
            is SessionEvent.UpdateSessionDate -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update { it.copy(
                        session = it.session.copy (
                            date = event.date
                        )
                    )}
                    repository.updateSession(_state.value.session)
                    _state.update { it.copy(
                        isDatePickerDialogOpen = false
                    )}
                }
            }
            SessionEvent.ExpandMovementSelectionSheet -> {
                _state.update {
                    it.copy(isMovementSelectionSheetOpen = true)
                }
            }
            SessionEvent.CollapseMovementSelectionSheet -> {
                _state.update {
                    it.copy(isMovementSelectionSheetOpen = false)
                }
            }
            is SessionEvent.NewExercise -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.insertExercise(
                        Exercise(
                            sessionId = _state.value.session.id,
                            movementId = event.movementId,
                            order = state.value.exercises.size + 1
                        )
                    )
                }
                _state.update {
                    it.copy(isMovementSelectionSheetOpen = false)
                }
            }
            is SessionEvent.DeleteExercise -> {
                /* TODO */
            }
            is SessionEvent.NewSet -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val newSetOrder = state.value.exercises
                        .flatMap { it.sets }
                        .count { it.exerciseId == event.exercise.id } + 1
                    val newSet = Set(
                        exerciseId = event.exercise.id,
                        order = newSetOrder
                    )
                    repository.insertSet(newSet)
                }
            }
            is SessionEvent.UpdateSet -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.updateSet(event.set)
                }
            }
            is SessionEvent.DeleteSet -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.deleteSet(event.set)
                    /* Update the order of the other sets if necessary */
                    state.value.exercises
                        .flatMap { it.sets }
                        .filter {
                            it.exerciseId == event.set.exerciseId && it.order > event.set.order
                        }.forEach {
                            repository.updateSet(it.copy(order = it.order - 1))
                        }
                }
            }
            is SessionEvent.FilterMovementsByName -> {
                _state.update {
                    it.copy(movementNameFilter = event.nameFilter)
                }
            }
        }
    }
}