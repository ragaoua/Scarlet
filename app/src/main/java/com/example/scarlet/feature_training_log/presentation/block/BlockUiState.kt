package com.example.scarlet.feature_training_log.presentation.block

import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.Session

data class BlockUiState (
    val block: Block = Block(),
    val sessions: List<Session> = emptyList()
)