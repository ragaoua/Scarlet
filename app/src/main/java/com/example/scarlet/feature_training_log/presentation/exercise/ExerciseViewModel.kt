package com.example.scarlet.feature_training_log.presentation.exercise

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.feature_training_log.data.repository.ScarletRepository
import com.example.scarlet.feature_training_log.presentation.destinations.ExerciseScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val repository: ScarletRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val exercise = MutableStateFlow(
        ExerciseScreenDestination.argsFrom(savedStateHandle).exercise
    )

    private val sets = repository.getSetsByExerciseId(exercise.value.id)

    val state = combine(exercise, sets) { exercise, sets ->
        ExerciseUiState(
            exercise = exercise,
            sets = sets
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ExerciseUiState())

    fun onEvent(event: ExerciseEvent) {
        /* TODO */
    }
}