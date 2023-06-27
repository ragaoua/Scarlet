package com.example.scarlet.feature_training_log.presentation.block

import com.example.scarlet.feature_training_log.domain.model.Session

sealed interface BlockEvent {
    object EndBlock: BlockEvent
    object AddSession: BlockEvent
    data class DeleteSession(val session: Session) :
        BlockEvent
}