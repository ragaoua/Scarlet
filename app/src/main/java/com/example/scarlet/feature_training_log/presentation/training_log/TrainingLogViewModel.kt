package com.example.scarlet.feature_training_log.presentation.training_log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.R
import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.use_case.training_log.TrainingLogUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TrainingLogViewModel @Inject constructor(
    private val useCases: TrainingLogUseCases
) : ViewModel() {

    private val _uiActions = Channel<UiAction>()
    val uiActions = _uiActions.receiveAsFlow()

    private val _state = MutableStateFlow(TrainingLogUiState())
    val state = _state.asStateFlow()

    private var blockCollectionJob: Job? = null

    private val DELETE_BLOCK_TIMEOUT = 5000L // Should be the same as the snackbar duration
//    private val latestDeletedBlock: MutableStateFlow<Block?> = MutableStateFlow(null)
    private var blockToRestoreOnUndo: Block? = null
    private var deleteBlockJob: Job? = null

    init {
        collectBlocks()
//        initLatestDeletedBlockCollection()
    }

    fun onEvent(event: TrainingLogEvent){
        when(event) {
            TrainingLogEvent.ShowNewBlockSheet -> {
                _state.update { it.copy(
                    newBlockSheetState = TrainingLogUiState.NewBlockSheetState()
                )}
            }
            TrainingLogEvent.HideNewBlockSheet -> {
                _state.update { it.copy(
                    newBlockSheetState = null
                )}
            }
            is TrainingLogEvent.UpdateNewBlockName -> {
                _state.update { it.copy(
                    newBlockSheetState = it.newBlockSheetState?.copy(
                        blockName = event.newBlockName
                    )
                )}
            }
            TrainingLogEvent.ToggleMicroCycleSettings -> {
                _state.update { it.copy(
                    newBlockSheetState = it.newBlockSheetState?.copy(
                        areMicroCycleSettingsExpanded = !it.newBlockSheetState.areMicroCycleSettingsExpanded
                    )
                )}
            }
            is TrainingLogEvent.UpdateDaysPerMicroCycle -> {
                _state.update { it.copy(
                    newBlockSheetState = it.newBlockSheetState?.copy(
                        daysPerMicroCycle = event.nbDays
                    )
                )}
            }
            is TrainingLogEvent.AddBlock -> {
                viewModelScope.launch(Dispatchers.IO) {
                    useCases.insertBlock(
                        blockName = event.blockName,
                        nbDays = state.value.newBlockSheetState?.let {
                            if(it.areMicroCycleSettingsExpanded) {
                                it.daysPerMicroCycle
                            } else 1
                        } ?: run {
                            // TODO Should we display an error here ?
                            return@launch
                        }
                    ).also { resource ->
                        resource.error?.let { error ->
                            _state.update { it.copy (
                                newBlockSheetState = it.newBlockSheetState?.copy(
                                    blockNameError = error
                                )
                            )}
                        }
                        resource.data?.let { insertedBlock ->
                            _uiActions.send(UiAction.NavigateToBlockScreen(insertedBlock))
                            _state.update { it.copy(
                                newBlockSheetState = null
                            )}
                        }
                    }
                }
            }
            is TrainingLogEvent.DeleteBlock -> {
                executeBlockDeletionImmediately()

                blockToRestoreOnUndo = event.block
                deleteBlockJob = viewModelScope.launch(Dispatchers.IO) {
                    delay(DELETE_BLOCK_TIMEOUT)
                    withContext(NonCancellable) {
                        blockToRestoreOnUndo = null
                        useCases.deleteBlock(event.block)
                    }
                }
                _state.update { state -> state.copy(
                    blocks = state.blocks
                        .filter { it.id != blockToRestoreOnUndo?.id }
                )}
                viewModelScope.launch {
                    _uiActions.send(UiAction.ShowSnackbar(
                        message = StringResource(R.string.block_deleted),
                        actionLabel = StringResource(R.string.undo),
                        onActionPerformed = {
                            onEvent(TrainingLogEvent.UndoDeleteBlock)
                        }
                    ))
                }
            }
            is TrainingLogEvent.UndoDeleteBlock -> {
                deleteBlockJob?.cancel()
                blockToRestoreOnUndo = null
                collectBlocks()
            }
        }
    }

    private fun collectBlocks() {
        blockCollectionJob?.cancel()
        blockCollectionJob = useCases.getAllBlocks()
            .onEach { resource ->
                _state.update { state -> state.copy(
                    blocks = resource.data
                        ?.filter { it.id != blockToRestoreOnUndo?.id }
                        ?: emptyList()
                )}
            }.launchIn(viewModelScope)
    }

    private fun executeBlockDeletionImmediately(scope: CoroutineScope = viewModelScope) {
        deleteBlockJob?.cancel()
        blockToRestoreOnUndo?.let { block ->
            blockToRestoreOnUndo = null
            scope.launch(Dispatchers.IO) {
                useCases.deleteBlock(block)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCleared() {
        super.onCleared()

        // TODO Should we use runBlocking instead of a GlobalScope when the view model is cleared ?
        executeBlockDeletionImmediately(scope = GlobalScope)
    }

    sealed interface UiAction {
        data class NavigateToBlockScreen(val block: Block): UiAction
        class ShowSnackbar(
            val message: StringResource,
            val actionLabel: StringResource? = null,
            val onActionPerformed: (() -> Unit)? = null
        ): UiAction
    }

}