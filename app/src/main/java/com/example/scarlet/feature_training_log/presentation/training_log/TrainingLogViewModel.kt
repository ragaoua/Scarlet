package com.example.scarlet.feature_training_log.presentation.training_log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.feature_training_log.data.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.BlockWithSessions
import com.example.scarlet.feature_training_log.presentation.core.DateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
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
        var activeBlock: BlockWithSessions? = null
        if (activeBlocks.size > 1) {
            throw Exception("Too many active blocks. Should only get one")
            /* TODO : catch the exception and displays an error message in the UI */
        } else if (activeBlocks.isNotEmpty()) {
            activeBlock = BlockWithSessions(
                block = activeBlocks.keys.toList()[0],
                sessions = activeBlocks.values.flatten()
                    .sortedBy {
                        LocalDate.parse(it.date, DateFormatter)
                    }
            )
        }
        state.copy(
            completedBlocks = completedBlocks.map {
                BlockWithSessions(
                    block = it.key,
                    sessions = it.value.sortedBy {
                        LocalDate.parse(it.date, DateFormatter)
                    }
                )
            }.sortedByDescending {
                LocalDate.parse(it.sessions.last().date, DateFormatter)
            },
            activeBlock = activeBlock
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), TrainingLogUiState())

    fun onEvent(event: TrainingLogEvent){
        when(event) {
            TrainingLogEvent.ShowNewBlockDialog -> {
                _state.update { it.copy(
                    isAddingBlock = true
                ) }
            }
            TrainingLogEvent.HideNewBlockDialog -> {
                _state.update { it.copy(
                    isAddingBlock = false
                ) }
            }
            is TrainingLogEvent.CreateBlock -> {
                val block = Block(name = event.blockName)
                viewModelScope.launch(Dispatchers.IO) {
                    repository.insertBlock(block)
                }
                _state.update { it.copy(
                    isAddingBlock = false
                ) }
            }
            is TrainingLogEvent.DeleteBlock -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.deleteBlock(event.block)
                }
            }
        }
    }

}