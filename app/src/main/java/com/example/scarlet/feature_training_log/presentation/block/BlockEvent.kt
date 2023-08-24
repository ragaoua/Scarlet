package com.example.scarlet.feature_training_log.presentation.block

import com.example.scarlet.feature_training_log.domain.model.Session

sealed interface BlockEvent {
    data class SaveBlockName(val blockName: String) : BlockEvent
    object AddSession: BlockEvent
    data class DeleteSession(val session: Session) : BlockEvent
    object EditBlock: BlockEvent
    data class UpdateEditedBlockName(val editedBlockName: String) : BlockEvent
    data class SelectDay(val dayId: Long) : BlockEvent
}