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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockViewModel @Inject constructor(
    private val repository: ScarletRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val block = MutableStateFlow(
        BlockScreenDestination.argsFrom(savedStateHandle).block
    )

    private val sessionWithMovementNames =
        repository.getSessionsWithMovementNamesByBlockId(block.value.id)

    val state = combine(block, sessionWithMovementNames) { block, sessions ->
        BlockUiState(
            block = block,
            sessionsWithMovement = sessions
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), BlockUiState())

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun onEvent(event: BlockEvent) {
        when(event) {
            BlockEvent.AddSession -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.insertSession(Session(blockId = block.value.id))
                }
            }
            BlockEvent.EndBlock -> {
                viewModelScope.launch(Dispatchers.IO) {
                    block.value.completed = true
                    repository.updateBlock(block.value)
                    _event.emit(UiEvent.NavigateUp)
                }
            }
            is BlockEvent.DeleteSession -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.deleteSession(event.session)
                }
            }
        }
    }
}