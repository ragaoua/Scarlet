package com.example.scarlet.feature_training_log.presentation.block

import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.Session

sealed interface BlockEvent {
    object AddSession: BlockEvent
    data class DeleteSession(val session: Session) : BlockEvent

    object EditBlock: BlockEvent
    object CancelBlockEdition: BlockEvent
    data class UpdateEditedBlockName(val editedBlockName: String) : BlockEvent
    object ToggleMicroCycleSettings: BlockEvent
    data class UpdateDaysPerMicroCycle(val nbDays: Int) : BlockEvent
    object SaveBlockName: BlockEvent

    data class SelectDay(val day: Day) : BlockEvent

    data class ShowMovementSelectionSheet(
        val session: Session,
        val exercise: Exercise? = null
    ) : BlockEvent
    object HideMovementSelectionSheet : BlockEvent

}