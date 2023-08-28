package com.example.scarlet.feature_training_log.presentation.training_log

import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.model.BlockWithDays
import com.example.scarlet.feature_training_log.domain.model.DayWithSessions
import com.example.scarlet.feature_training_log.domain.model.Session

data class TrainingLogUiState (
    val blocks: List<BlockWithDays<DayWithSessions<Session>>> = emptyList(),
    val newBlockSheetState: NewBlockSheetState? = null // null means the sheet is hidden
) {
    data class NewBlockSheetState(
        val blockName: String = "",
        val blockNameError: StringResource? = null,
        val areMicroCycleSettingsExpanded: Boolean = false,
        val daysPerMicroCycle: Int = 3
    )
}