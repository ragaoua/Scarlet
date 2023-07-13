package com.example.scarlet.feature_training_log.presentation.training_log

import com.example.scarlet.feature_training_log.domain.model.BlockWithDates

data class TrainingLogUiState (
    val activeBlock: BlockWithDates? = null,
    val completedBlocks: List<BlockWithDates> = emptyList(),
    val isAddingBlock: Boolean = false,
    val isShowingBlockNameEmptyMsg: Boolean = false
)