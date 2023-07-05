package com.example.scarlet.feature_training_log.presentation.session

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.feature_training_log.data.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Movement
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


    /*
    When updating/deleting a set, we test for the order of the set, and not the id, in case
    the set hasn't been saved to the database yet. In that case, the id will be 0, so if we
    check for the id, we would delete/update all the sets with an id of 0 (ie all the sets
    that have just been added, but not persisted to the database yet).
     */
    fun onEvent(event: SessionEvent) {
        when (event) {
            is SessionEvent.NewExercise -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        exercises = it.exercises + ExerciseWithMovementAndSets(
                            exercise = Exercise(
                                sessionId = it.session.id,
                                order = it.exercises.size + 1
                            ),
                            movement = Movement(name = event.exerciseName),
                            sets = emptyList()
                        )
                    ) }
                }
            }
            is SessionEvent.DeleteExercise -> {
                /* TODO */
            }
            is SessionEvent.NewSet -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        exercises = it.exercises.map { exercise ->
                            if (exercise.exercise.id == event.exercise.id) {
                                exercise.copy(
                                    sets = exercise.sets + Set(
                                        exerciseId = event.exercise.id,
                                        order = exercise.sets.size + 1
                                    )
                                )
                            } else {
                                exercise
                            }
                        }
                    ) }
                }
            }
            is SessionEvent.UpdateSet -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        exercises = it.exercises.map { exercise ->
                            if (exercise.exercise.id == event.set.exerciseId) {
                                exercise.copy(
                                    sets = exercise.sets.map { set ->
                                        if (set.order == event.set.order) {
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
            is SessionEvent.DeleteSet -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        exercises = it.exercises.map { exercise ->
                            if (exercise.exercise.id == event.set.exerciseId) {
                                exercise.copy(
                                    sets = exercise.sets.filter { set ->
                                        set.order != event.set.order
                                    }.map { set ->
                                        /* Update the order of the remaining sets if necessary */
                                        if (set.order > event.set.order) {
                                            set.copy(order = set.order - 1)
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
                    /* If the set has been saved to the database, we want to delete it */
                    if (event.set.id != 0) {
                        deletedSets.add(event.set)
                    }
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
        }
    }
}