package com.example.scarlet.feature_training_log.presentation.training_log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.R
import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.BlockWithDays
import com.example.scarlet.feature_training_log.domain.model.DayWithSessions
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercises
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingLogViewModel @Inject constructor(
    private val useCases: TrainingLogUseCases
) : ViewModel() {

    private val _uiActions = Channel<UiAction>()
    val uiActions = _uiActions.receiveAsFlow()

    private val _state = MutableStateFlow(TrainingLogUiState())
    val state = _state.asStateFlow()

    private var blockToRestoreOnUndo:
            BlockWithDays<DayWithSessions<SessionWithExercises<ExerciseWithMovementAndSets>>>? = null

    init {
        collectBlocks()
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
                        } ?: return@launch
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
                viewModelScope.launch(Dispatchers.IO) {
                    blockToRestoreOnUndo =
                        BlockWithDays(
                            id = event.block.id,
                            name = event.block.name,
                            days = useCases.getDaysWithSessionsWithExercisesWithMovementAndSetsByBlockId(
                                event.block.id
                            ).first().data ?: return@launch
                        )

                    useCases.deleteBlock(event.block)

                    _uiActions.send(UiAction.ShowSnackbar(
                        message = StringResource(R.string.block_deleted),
                        actionLabel = StringResource(R.string.undo),
                        onActionPerformed = {
                            blockToRestoreOnUndo?.let {
                                blockToRestoreOnUndo = null
                                viewModelScope.launch(Dispatchers.IO) {
                                    useCases.restoreBlockWithDaysWithSessionsWithExercisesWithSets(it)
                                }
                            }
                        }
                    ))
                }
            }
        }
    }

    private fun collectBlocks() {
        useCases.getAllBlocks()
            .onEach { resource ->
                _state.update { state -> state.copy(
                    blocks = resource.data ?: emptyList()
                )}
            }.launchIn(viewModelScope)
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