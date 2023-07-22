package com.example.scarlet.feature_training_log.presentation.block

import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.SessionWithMovements

data class BlockUiState (
    val block: Block = Block(),
    val sessionsWithMovements: List<SessionWithMovements> = emptyList(),
    val isInEditMode: Boolean = false
)