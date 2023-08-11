package com.example.scarlet.feature_training_log.presentation.training_log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.use_case.training_log.TrainingLogUseCases
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
class TrainingLogViewModel @Inject constructor(
    private val useCases: TrainingLogUseCases
) : ViewModel() {

    private val _uiActions = MutableSharedFlow<UiAction>()
    val uiActions = _uiActions.asSharedFlow()

    private val _state = MutableStateFlow(TrainingLogUiState())
    val state = _state.asStateFlow()

    init {
        initBlocksCollection()
    }

    fun onEvent(event: TrainingLogEvent){
        when(event) {
            TrainingLogEvent.ToggleNewBlockSheet -> {
                _state.update { it.copy(
                    isNewBlockSheetExpanded = !it.isNewBlockSheetExpanded,
                    newBlockSheetTextFieldError = null
                )}
            }
            is TrainingLogEvent.AddBlock -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val block = Block(name = event.blockName)
                    useCases.insertBlock(block)
                        .also { resource ->
                            resource.error?.let { error ->
                                _state.update { it.copy (
                                    newBlockSheetTextFieldError = error
                                )}
                            }
                            resource.data?.let { insertedBlockId ->
                                _uiActions.emit(UiAction.NavigateToBlockScreen(
                                    block = block.copy(
                                        id = insertedBlockId
                                    )
                                ))
                                _state.update { it.copy(
                                    isNewBlockSheetExpanded = false,
                                    newBlockSheetTextFieldError = null
                                )}
                            }
                        }
                }
            }
            is TrainingLogEvent.DeleteBlock -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.deleteBlock(event.block)
                }
            }
        }
    }

    private fun initBlocksCollection() {
        useCases.getActiveBlock()
            .onEach { resource ->
                resource.error?.let { error ->
                    _uiActions.emit(UiAction.ShowSnackbarWithError(error))
                } ?: run {
                    _state.update { it.copy(
                        activeBlock = resource.data
                    )}
                }
            }.launchIn(viewModelScope)

        useCases.getCompletedBlocks()
            .onEach { resource ->
                _state.update { it.copy(
                    completedBlocks = resource.data ?: emptyList()
                )}
            }.launchIn(viewModelScope)
    }

    sealed interface UiAction {
        data class NavigateToBlockScreen(val block: Block): UiAction
        class ShowSnackbarWithError(val error: StringResource): UiAction
    }

}