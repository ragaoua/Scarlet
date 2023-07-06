package com.example.scarlet.feature_training_log.presentation.session

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.feature_training_log.data.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.presentation.destinations.SessionScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val repository: ScarletRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val session = SessionScreenDestination.argsFrom(savedStateHandle).session
    private val exercises = repository.getExercisesWithMovementAndSetsBySessionId(session.id)

    val state = exercises.map { sessions ->
        SessionUiState(
            session = session,
            exercises = sessions
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SessionUiState(
            session = session,
            exercises = emptyList()
        )
    )

    fun onEvent(event: SessionEvent) {
        when (event) {
            is SessionEvent.NewExercise -> {
                /* TODO */
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
        }
    }
}