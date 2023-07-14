package com.example.scarlet.feature_training_log.presentation.block

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.feature_training_log.data.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.presentation.destinations.BlockScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockViewModel @Inject constructor(
    private val repository: ScarletRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val block = BlockScreenDestination.argsFrom(savedStateHandle).block

    private val sessionWithMovementNames =
        repository.getSessionsWithMovementNamesByBlockId(block.id)

    private val _state = MutableStateFlow(
        BlockUiState(block = block)
    )

    val state = combine(_state, sessionWithMovementNames) { state, sessions ->
        state.copy(
            sessionsWithMovement = sessions
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), BlockUiState())

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun onEvent(event: BlockEvent) {
        when(event) {
            BlockEvent.AddSession -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.insertSession(Session(blockId = _state.value.block.id))
                }
            }
            BlockEvent.EndBlock -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.value.block.completed = true
                    repository.updateBlock(_state.value.block)
                    _event.emit(UiEvent.NavigateUp)
                }
            }
            is BlockEvent.DeleteSession -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.deleteSession(event.session)
                }
            }
            BlockEvent.EditBlockClicked -> {
                _state.update {
                    it.copy(
                        isEditing = true
                    )
                }
            }
            is BlockEvent.UpdateBlock -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if (_state.value.block != event.block && event.block.name.isNotBlank()) {
                        repository.updateBlock(event.block)
                        _state.update {
                            it.copy(
                                block = event.block
                            )
                        }
                    }
                    _state.update {
                        it.copy(
                            isEditing = false
                        )
                    }
                }
            }
        }
    }
}