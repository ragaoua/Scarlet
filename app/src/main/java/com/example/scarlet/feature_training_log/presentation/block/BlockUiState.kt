package com.example.scarlet.feature_training_log.presentation.block

import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.model.DayWithSessionsWithExercisesWithMovement

data class BlockUiState (
    val block: Block = Block(),
    val days: List<DayWithSessionsWithExercisesWithMovement> = emptyList(),
    val selectedDay: Day? = null,

    val editBlockSheetState: EditBlockSheetState? = null // null means the sheet is hidden
) {
    data class EditBlockSheetState(
        val blockName: String = "",
        val blockNameError: StringResource? = null,
        val areMicroCycleSettingsExpanded: Boolean = false,
        val daysPerMicroCycle: Int = 3
    )
}