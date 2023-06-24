package com.example.scarlet.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.db.ScarletRepository
import com.example.scarlet.ui.events.ExerciseEvent
import com.example.scarlet.ui.screen.destinations.ExerciseScreenDestination
import com.example.scarlet.ui.states.ExerciseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val repository: ScarletRepository,
    private val savedStateHandle: SavedStateHandle
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