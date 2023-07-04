package com.example.scarlet.feature_training_log.presentation.session

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.feature_training_log.data.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.presentation.destinations.SessionScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val repository: ScarletRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(SessionUiState())
    val state = _state.asStateFlow()

    private val deletedSets: MutableList<Set> = mutableListOf()

    init {
        viewModelScope.launch {
            val session = SessionScreenDestination.argsFrom(savedStateHandle).session
            _state.update {
                SessionUiState(
                    session = session,
                    exercises = repository.getExercisesWithMovementAndSetsBySessionId(session.id)
                )
            }
        }
    }

    fun onEvent(event: SessionEvent) {
        when (event) {
            is SessionEvent.UpdateSet -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        exercises = it.exercises.map { exercise ->
                            if (exercise.exercise.id == event.set.exerciseId) {
                                exercise.copy(
                                    sets = exercise.sets.map { set ->
                                        if (set.id == event.set.id) {
                                            set.copy(
                                                reps = event.reps,
                                                weight = event.weight,
                                                rpe = event.rpe
                                            )
                                        } else {
                                            set
                                        }
                                    }
                                )
                            } else {
                                exercise
                            }
                        }
                    ) }
                }
            }
            is SessionEvent.NewSet -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        exercises = it.exercises.map { exercise ->
                            if (exercise.exercise.id == event.exercise.id) {
                                exercise.copy(
                                    sets = exercise.sets + Set(
                                        exerciseId = event.exercise.id,
                                        order = (exercise.sets.lastOrNull()?.order ?: 0) + 1
                                    )
                                )
                            } else {
                                exercise
                            }
                        }
                    ) }
                }
            }
            is SessionEvent.DeleteSet -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        exercises = it.exercises.map { exercise ->
                            if (exercise.exercise.id == event.set.exerciseId) {
                                exercise.copy(
                                    sets = exercise.sets.filter { set ->
                                        set.id != event.set.id
                                    }
                                )
                            } else {
                                exercise
                            }
                        }
                    ) }
                    deletedSets.add(event.set)
                }
            }
            SessionEvent.SaveSession -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.updateSession(state.value.session)

                    state.value.exercises.forEach { exercise ->
                        repository.upsertExercise(exercise.exercise)
                        exercise.sets.forEach {
                            repository.upsertSet(it)
                        }
                    }

                    deletedSets.forEach{
                        repository.deleteSet(it)
                    }
                }
            }
            else -> { /* TODO */}
        }
    }
}