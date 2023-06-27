package com.example.scarlet.feature_training_log.presentation.training_log

import com.example.scarlet.feature_training_log.domain.model.Block

data class TrainingLogUiState (
    val activeBlock: Block? = null,
    val completedBlocks: List<Block> = emptyList(),
    val isAddingBlock: Boolean = false,
    val isShowingBlockNameEmptyMsg: Boolean = false
)