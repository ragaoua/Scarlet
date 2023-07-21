package com.example.scarlet.feature_training_log.presentation.training_log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.use_case.training_log.GetActiveBlockUseCase
import com.example.scarlet.feature_training_log.domain.use_case.training_log.TrainingLogUseCases
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
class TrainingLogViewModel @Inject constructor(
    private val repository: ScarletRepository,
    useCases: TrainingLogUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(TrainingLogUiState())
    val state = combine(
        _state,
        useCases.getActiveBlock(),
        useCases.getCompletedBlocks()
    ) { state, activeBlockResource, completedBlockResource ->
        if(activeBlockResource is Resource.Error<*,*>) {
            when(activeBlockResource.error) {
                is GetActiveBlockUseCase.ActiveBlockErrors.TooManyActiveBlocks -> {
                    /*
                     * TODO : Emit a UI event that will be handled by the UI layer
                     * Idea : Show a dialog that will ask the user to delete one of the active
                     *        blocks ?
                     * Idea : automatically correct by updating the database and setting completed
                     *        to true for all but the last active block ? In that case, show a
                     *        dialog to inform the user of the operation afterwards
                     */
                }
            }
        }
        state.copy(
            activeBlock = activeBlockResource.data,
            completedBlocks = completedBlockResource.data ?: emptyList()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), TrainingLogUiState())

    private val _event = MutableSharedFlow<TrainingLogViewModelUiEvent>()
    val event = _event.asSharedFlow()

    fun onEvent(event: TrainingLogEvent){
        when(event) {
            TrainingLogEvent.ShowNewBlockSheet -> {
                _state.update { it.copy(
                    isNewBlockSheetExpanded = true
                )}
            }
            TrainingLogEvent.HideNewBlockSheet -> {
                _state.update { it.copy(
                    isNewBlockSheetExpanded = false
                )}
            }
            is TrainingLogEvent.CreateBlock -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val insertedBlock = Block(name = event.blockName)
                    val insertedBlockId = repository.insertBlock(insertedBlock)
                    _event.emit(TrainingLogViewModelUiEvent.NavigateToBlockScreen(
                        block = insertedBlock.copy(
                            id = insertedBlockId.toInt()
                        )
                    ))
                }
                _state.update { it.copy(
                    isNewBlockSheetExpanded = false
                )}
            }
            is TrainingLogEvent.DeleteBlock -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.deleteBlock(event.block)
                }
            }
        }
    }

}