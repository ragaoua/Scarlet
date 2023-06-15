package com.example.scarlet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scarlet.db.ScarletRepository
import com.example.scarlet.db.model.Block
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingLogViewModel @Inject constructor(
    private val repository: ScarletRepository
) : ViewModel() {

    val completedBlocks = repository.getBlocksByCompleted(true)

    private val _activeBlocks = repository.getBlocksByCompleted(false)
    private val _activeBlock: MutableStateFlow<Block?> = MutableStateFlow(null)
    val activeBlock = _activeBlock.asStateFlow()
    private fun updateActiveBlock() {
        viewModelScope.launch(Dispatchers.IO) {
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

    fun getSessionsByBlockId(blockId: Int) = repository.getSessionsByBlockId(blockId)

    fun getExercisesBySessionId(sessionId: Int) = repository.getExercisesBySessionId(sessionId)

    fun getExerciseSetsById(exerciseId: Int) = repository.getExerciseSetsById(exerciseId)

    init {
        updateActiveBlock()
    }
}