package com.example.scarlet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.db.ScarletRepository
import com.example.scarlet.model.Block
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TrainingLogViewModel(
    repository: ScarletRepository
) : ViewModel() {

    val completedBlocks = repository.getBlocksByCompleted(true)

    private val _activeBlocks = repository.getBlocksByCompleted(false)
    private val _activeBlock: MutableStateFlow<Block?> = MutableStateFlow(null)
    val activeBlock: StateFlow<Block?> = _activeBlock
    init {
        viewModelScope.launch {
            _activeBlocks.collect { blocks ->
                if (blocks.isEmpty()) {
                    _activeBlock.value = null
                } else {
                    if (blocks.size > 1) {
                        throw Exception("Too many active blocks. Should only get one")
                    } else {
                        _activeBlock.value = blocks[0]
                    }
                }
            }
        }
    }

    private val _displayedBlock: MutableStateFlow<Block?> = MutableStateFlow(null)
    val displayedBlock: StateFlow<Block?> = _displayedBlock
    fun setDisplayedBlock(block: Block?) {
        _displayedBlock.value = block
    }
}