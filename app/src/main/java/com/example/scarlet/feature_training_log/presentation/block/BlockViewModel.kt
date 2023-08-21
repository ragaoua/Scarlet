package com.example.scarlet.feature_training_log.presentation.block

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.use_case.block.BlockUseCases
import com.example.scarlet.feature_training_log.presentation.destinations.BlockScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockViewModel @Inject constructor(
    private val useCases : BlockUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiActions = MutableSharedFlow<UiAction>()
    val uiActions = _uiActions.asSharedFlow()

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
                    useCases.insertSession(state.value.block.id)
                        .also { resource ->
                            resource.data?.let { insertedSession ->
                                _uiActions.emit(UiAction.NavigateToSessionScreen(insertedSession))
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
                                _uiActions.emit(UiAction.ShowSnackbarWithError(error))
                            } ?: run {
                                _state.update { it.copy(
                                    block = updatedBlock
                                )}
                                _uiActions.emit(UiAction.NavigateUp)
                            }
                        }
                    }
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
                            _uiActions.emit(UiAction.ShowSnackbarWithError(error))
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
        useCases.getDaysWithSessionsWithMovementsByBlockId(block.id)
            .onEach { resource ->
                _state.update { it.copy(
                    days = resource.data ?: emptyList()
                )}
            }.launchIn(viewModelScope)
    }

    sealed interface UiAction {
        object NavigateUp: UiAction
        data class NavigateToSessionScreen(val session: Session): UiAction
        class ShowSnackbarWithError(val error: StringResource): UiAction
    }
}