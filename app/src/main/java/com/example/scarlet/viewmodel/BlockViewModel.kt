package com.example.scarlet.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.db.ScarletRepository
import com.example.scarlet.db.model.Session
import com.example.scarlet.events.BlockEvent
import com.example.scarlet.ui.screen.destinations.BlockScreenDestination
import com.example.scarlet.ui.states.BlockUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockViewModel @Inject constructor(
    private val repository: ScarletRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val block = MutableStateFlow(
        BlockScreenDestination.argsFrom(savedStateHandle).block
    )

    private val sessions = repository.getSessionsByBlockId(block.value.id)

    val state = combine(block, sessions) { block, sessions ->
        BlockUiState(
            block = block,
            sessions = sessions
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), BlockUiState())

    fun onEvent(event: BlockEvent) {
        when(event) {
            BlockEvent.AddSession -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.insertSession(Session(blockId = state.value.block.id))
                }
            }
            BlockEvent.EndBlock -> {
                viewModelScope.launch(Dispatchers.IO) {
                    state.value.block.completed = true
                    repository.updateBlock(state.value.block)
                }
            }
        }
    }
}