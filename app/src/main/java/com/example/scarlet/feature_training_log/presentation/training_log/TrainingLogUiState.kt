package com.example.scarlet.feature_training_log.presentation.training_log

import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.model.BlockWithSessions

data class TrainingLogUiState (
    val activeBlock: BlockWithSessions? = null,
    val completedBlocks: List<BlockWithSessions> = emptyList(),
    val newBlockName: String = "",
    val isNewBlockSheetExpanded: Boolean = false,
    val newBlockSheetTextFieldError: StringResource? = null
)