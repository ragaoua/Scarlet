package com.example.scarlet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.events.TrainingLogEvent
import com.example.scarlet.events.TrainingLogEvent.CreateBlock
import com.example.scarlet.events.TrainingLogEvent.HideNewBlockDialog
import com.example.scarlet.events.TrainingLogEvent.ShowNewBlockDialog
import com.example.scarlet.db.ScarletRepository
import com.example.scarlet.db.model.Block
import com.example.scarlet.ui.states.TrainingLogUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingLogViewModel @Inject constructor(
    private val repository: ScarletRepository
) : ViewModel() {

    private val completedBlocks = repository.getBlocksByCompleted(true)
    private val activeBlocks = repository.getBlocksByCompleted(false)

    private val _state = MutableStateFlow(TrainingLogUiState())
    val state = combine(_state, activeBlocks, completedBlocks) { state, activeBlocks, completedBlocks ->
        var activeBlock: Block? = null
        if (activeBlocks.size > 1) {
            throw Exception("Too many active blocks. Should only get one")
        } else if (activeBlocks.isNotEmpty()) {
            activeBlock = activeBlocks[0]
        }
        state.copy(
            completedBlocks = completedBlocks,
            activeBlock = activeBlock
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), TrainingLogUiState())

    fun onEvent(event: TrainingLogEvent){
        when(event) {
            ShowNewBlockDialog -> {
                _state.update { it.copy(
                    isAddingBlock = true
                ) }
            }
            HideNewBlockDialog -> {
                _state.update { it.copy(
                    isAddingBlock = false
                ) }
            }
            is CreateBlock -> {
                val block = Block(name = event.blockName)
                viewModelScope.launch(Dispatchers.IO) {
                    repository.insertBlock(block)
                }
                _state.update { it.copy(
                    isAddingBlock = false
                ) }
            }
        }
    }

}