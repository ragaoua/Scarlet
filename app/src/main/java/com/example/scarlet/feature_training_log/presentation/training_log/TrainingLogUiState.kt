package com.example.scarlet.feature_training_log.presentation.training_log

import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.model.BlockWithSessions
import com.example.scarlet.feature_training_log.domain.model.Session

data class TrainingLogUiState (
    val blocks: List<BlockWithSessions<Session>> = emptyList(),
    val newBlockSheetState: NewBlockSheetState? = null // null means the sheet is hidden
) {
    data class NewBlockSheetState(
        val blockName: String = "",
        val blockNameError: StringResource? = null,
        val areMicroCycleSettingsExpanded: Boolean = false,
        val daysPerMicroCycle: Int = 3
    )
}