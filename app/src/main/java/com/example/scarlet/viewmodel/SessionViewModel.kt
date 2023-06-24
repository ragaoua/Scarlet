package com.example.scarlet.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.db.ScarletRepository
import com.example.scarlet.ui.events.SessionEvent
import com.example.scarlet.ui.screen.destinations.SessionScreenDestination
import com.example.scarlet.ui.states.SessionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val repository: ScarletRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val session = MutableStateFlow(
        SessionScreenDestination.argsFrom(savedStateHandle).session
    )

    private val exercises = repository.getExercisesBySessionId(session.value.id)

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