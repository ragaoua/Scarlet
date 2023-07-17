package com.example.scarlet.feature_training_log.presentation.training_log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.feature_training_log.data.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.presentation.core.DateFormatter
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
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TrainingLogViewModel @Inject constructor(
    private val repository: ScarletRepository
) : ViewModel() {

    private val completedBlocks = repository.getBlocksWithSessionsByCompleted(true)
    private val activeBlocks = repository.getBlocksWithSessionsByCompleted(false)

    private val _state = MutableStateFlow(TrainingLogUiState())
    val state = combine(_state, activeBlocks, completedBlocks) { state, activeBlocks, completedBlocks ->
        state.copy(
            activeBlock =
                if (activeBlocks.size > 1) {
                    throw Exception("Too many active blocks. Should only get one")
                    /* TODO : catch the exception and displays an error message in the UI */
                } else {
                    activeBlocks.firstOrNull()?.copy(
                        sessions = activeBlocks.first().sessions
                            .sortedBy {
                                LocalDate.parse(it.date, DateFormatter)
                            }
                    )
                },
            completedBlocks =
                completedBlocks.map { completedBlock ->
                    completedBlock.copy(
                        sessions = completedBlock.sessions.sortedBy {
                            LocalDate.parse(it.date, DateFormatter)
                        }
                    )
                }.sortedByDescending {
                    it.sessions.lastOrNull()?.let { lastSession ->
                        LocalDate.parse(lastSession.date, DateFormatter)
                    }
                }
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