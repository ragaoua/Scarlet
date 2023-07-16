package com.example.scarlet.feature_training_log.presentation.training_log

import com.example.scarlet.feature_training_log.domain.model.BlockWithSessions

data class TrainingLogUiState (
    val activeBlock: BlockWithSessions? = null,
    val completedBlocks: List<BlockWithSessions> = emptyList(),
    val isNewBlockSheetExpanded: Boolean = false
)