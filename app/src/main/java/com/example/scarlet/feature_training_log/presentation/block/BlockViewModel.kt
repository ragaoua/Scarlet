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

    private val _uiActions = Channel<UiAction>()
    val uiActions = _uiActions.receiveAsFlow()

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
                    useCases.insertSession(state.value.selectedDayId)
                        .also { resource ->
                            resource.data?.let { insertedSession ->
                                _uiActions.send(UiAction.NavigateToSessionScreen(insertedSession))
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
                    isInEditMode = true,
                    editedBlockName = state.value.block.name
                )}
            }
            is BlockEvent.UpdateEditedBlockName -> {
                _state.update { it.copy(
                    editedBlockName = event.editedBlockName
                )}
            }
            is BlockEvent.SaveBlock -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.updateBlock(event.block).also { resource ->
                        resource.error?.let { error ->
                            _uiActions.send(UiAction.ShowSnackbarWithError(error))
                        } ?: run {
                            _state.update { it.copy(
                                block = event.block,
                                isInEditMode = false
                            )}
                        }
                    }
                }
            }
            is BlockEvent.SelectDay -> {
                _state.update { it.copy(
                    selectedDayId = event.dayId
                )}
            }
        }
    }

    private fun initBlockSessionsCollection() {
        useCases.getDaysWithSessionsWithMovementsByBlockId(block.id)
            .onEach { resource ->
                val days = resource.data ?: emptyList()
                _state.update { state ->
                    state.copy(
                        days = days,
                        selectedDayId =
                            if (state.selectedDayId in days.map { it.day.id }) {
                                state.selectedDayId
                            } else {
                                days.firstOrNull()?.day?.id ?: 0L
                            }
                    )
                }
            }.launchIn(viewModelScope)
    }

    sealed interface UiAction {
        object NavigateUp: UiAction
        data class NavigateToSessionScreen(val session: Session): UiAction
        class ShowSnackbarWithError(val error: StringResource): UiAction
    }
}