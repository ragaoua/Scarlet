package com.example.scarlet.feature_training_log.presentation.session

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.feature_training_log.data.repository.ScarletRepository
import com.example.scarlet.feature_training_log.presentation.destinations.SessionScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val repository: ScarletRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val session = MutableStateFlow(
        SessionScreenDestination.argsFrom(savedStateHandle).session
    )

    private val exercises = repository.getExercisesWithMovementAndSetsBySessionId(session.value.id)

    val state = combine(session, exercises) { session, exercises ->
        SessionUiState(
            session = session,
            exercises = exercises
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SessionUiState())

    fun onEvent(event: SessionEvent) {
        /* TODO */
    }
}