package com.example.scarlet.ui.states

import com.example.scarlet.db.model.Block

data class TrainingLogUiState (
    val activeBlock: Block? = null,
    val completedBlocks: List<Block> = emptyList(),
    val isAddingBlock: Boolean = false,
    val isShowingBlockNameEmptyMsg: Boolean = false
)