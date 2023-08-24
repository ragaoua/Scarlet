package com.example.scarlet.feature_training_log.presentation.block

import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.model.DayWithSessionsWithExercisesWithMovementName

data class BlockUiState (
    val block: Block = Block(),
    val days: List<DayWithSessionsWithExercisesWithMovementName> = emptyList(),
    val selectedDay: Day? = null,
    val isInEditMode: Boolean = false,
    val editedBlockName: String = ""
)