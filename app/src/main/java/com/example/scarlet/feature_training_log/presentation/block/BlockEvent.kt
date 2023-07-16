package com.example.scarlet.feature_training_log.presentation.block

import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.Session

sealed interface BlockEvent {
    data class UpdateBlock(val block: Block) : BlockEvent
    object EndBlock: BlockEvent
    object AddSession: BlockEvent
    data class DeleteSession(val session: Session) : BlockEvent
    object EditBlock: BlockEvent
}