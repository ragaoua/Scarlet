package com.example.scarlet.feature_training_log.presentation.block

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.use_case.block.BlockUseCases
import com.example.scarlet.feature_training_log.presentation.destinations.BlockScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockViewModel @Inject constructor(
    private val useCases : BlockUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _channel = Channel<UiAction>()
    val channel = _channel.receiveAsFlow()

    private val block = BlockScreenDestination.argsFrom(savedStateHandle).block

    private val _state = MutableStateFlow(BlockUiState(block = block))
    val state = _state.asStateFlow()

    init {
        initBlockSessionsCollection()
    }

    fun onEvent(event: BlockEvent) {
        when(event) {
            BlockEvent.AddSession -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val insertedSession = Session(blockId = state.value.block.id)
                    useCases.insertSession(insertedSession)
                        .also { insertedSessionResource ->
                            insertedSessionResource.data?.let { insertedSessionId ->
                                _channel.send(UiAction.NavigateToSessionScreen(
                                    insertedSession.copy(
                                        id = insertedSessionId.toInt()
                                    )
                                ))
                            }
                        }
                }
            }
            BlockEvent.EndBlock -> {
                viewModelScope.launch(Dispatchers.IO) {
                    state.value.block.copy(
                        completed = true
                    ).also { updatedBlock ->
                        useCases.updateBlock(updatedBlock).also { resource ->
                            resource.error?.let { error ->
                                // TODO: Handle this error
                            } ?: run {
                                _state.update { it.copy(
                                    block = updatedBlock
                                )}
                            }
                        }
                    }
                    _channel.send(UiAction.NavigateUp)
                }
            }
            is BlockEvent.DeleteSession -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.deleteSession(event.session)
                }
            }
            BlockEvent.EditBlock -> {
                _state.update { it.copy(
                    isInEditMode = true
                )}
            }
            is BlockEvent.UpdateBlock -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.updateBlock(event.block).also { resource ->
                        resource.error?.let { error ->
                            // TODO: Handle this error
                        } ?: run {
                            _state.update { it.copy(
                                block = event.block,
                                isInEditMode = false
                            )}
                        }
                    }
                }
            }
        }
    }

    private fun initBlockSessionsCollection() {
        useCases.getSessionsWithMovementsByBlockId(block.id)
            .onEach { resource ->
                _state.update { it.copy(
                    sessions = resource.data ?: emptyList()
                )}
            }.launchIn(viewModelScope)
    }

    sealed interface UiAction {
        object NavigateUp: UiAction
        data class NavigateToSessionScreen(val session: Session): UiAction
    }
}