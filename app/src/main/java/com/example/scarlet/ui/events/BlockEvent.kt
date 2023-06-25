package com.example.scarlet.ui.events

import com.example.scarlet.db.model.Session

sealed interface BlockEvent {
    object EndBlock: BlockEvent
    object AddSession: BlockEvent
    data class DeleteSession(val session: Session) : BlockEvent
}