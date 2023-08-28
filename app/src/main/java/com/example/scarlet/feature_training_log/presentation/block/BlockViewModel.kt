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

    private val _uiActions = Channel<UiAction>()
    val uiActions = _uiActions.receiveAsFlow()

    private val _state = MutableStateFlow(BlockUiState(
        block = BlockScreenDestination.argsFrom(savedStateHandle).block
    ))
    val state = _state.asStateFlow()

    init {
        initBlockSessionsCollection()
    }

    fun onEvent(event: BlockEvent) {
        when(event) {
            BlockEvent.AddSession -> {
                viewModelScope.launch(Dispatchers.IO) {
                    state.value.selectedDay?.let { selectedDay ->
                        useCases.insertSession(selectedDay.id)
                            .also { resource ->
                                resource.data?.let { insertedSession ->
                                    _uiActions.send(UiAction.NavigateToSessionScreen(insertedSession))
                                }
                            }
                    } // TODO should we print an error if selectedDay is null?
                }
            }
            is BlockEvent.DeleteSession -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.deleteSession(event.session)
                }
            }
            BlockEvent.EditBlock -> {
                _state.update { it.copy(
                    editBlockSheetState = BlockUiState.EditBlockSheetState(
                        blockName = state.value.block.name,
                        areMicroCycleSettingsExpanded = false, // TODO
                        daysPerMicroCycle = 3 // TODO
                    )
                )}
            }
            BlockEvent.CancelBlockEdition -> {
                _state.update { it.copy(
                    editBlockSheetState = null
                )}
            }
            is BlockEvent.UpdateEditedBlockName -> {
                _state.update { it.copy(
                    editBlockSheetState = it.editBlockSheetState?.copy(
                        blockName = event.editedBlockName
                    )
                )}
            }
            BlockEvent.ToggleMicroCycleSettings -> {
                _state.update { it.copy(
                    editBlockSheetState = it.editBlockSheetState?.copy(
                        areMicroCycleSettingsExpanded = !it.editBlockSheetState.areMicroCycleSettingsExpanded
                    )
                )}
            }
            is BlockEvent.UpdateDaysPerMicroCycle -> {
                _state.update { it.copy(
                    editBlockSheetState = it.editBlockSheetState?.copy(
                        daysPerMicroCycle = event.nbDays
                    )
                )}
            }
            is BlockEvent.SaveBlockName -> {
                viewModelScope.launch(Dispatchers.IO) {
                    state.value.editBlockSheetState?.blockName?.let { editedBlockName ->
                        if(editedBlockName == state.value.block.name) {
                            _state.update { it.copy(
                                editBlockSheetState = null
                            )}
                            return@launch
                        }

                        val updatedBlock = state.value.block.copy(
                            name = editedBlockName
                        )
                        useCases.updateBlock(updatedBlock).also { resource ->
                            resource.error?.let { error ->
                                _state.update { it.copy (
                                    editBlockSheetState = it.editBlockSheetState?.copy(
                                        blockNameError = error
                                    )
                                )}
                            } ?: run {
                                _state.update { it.copy(
                                    block = updatedBlock,
                                    editBlockSheetState = null
                                )}
                            }
                        }
                    }
                }
            }
            is BlockEvent.SelectDay -> {
                _state.update { it.copy(
                    selectedDay = event.day
                )}
            }
        }
    }

    private fun initBlockSessionsCollection() {
        useCases.getDaysWithSessionsWithMovementsByBlockId(state.value.block.id)
            .onEach { resource ->
                val days = resource.data ?: emptyList()
                _state.update { state ->
                    state.copy(
                        days = days,
                        selectedDay =
                            if (state.selectedDay in days.map { it.toDay() }) {
                                state.selectedDay
                            } else {
                                days.firstOrNull()?.toDay()
                            }
                    )
                }
            }.launchIn(viewModelScope)
    }

    sealed interface UiAction {
        object NavigateUp: UiAction
        data class NavigateToSessionScreen(val session: Session): UiAction
    }
}