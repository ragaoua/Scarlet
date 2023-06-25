package com.example.scarlet.ui.events

import com.example.scarlet.db.model.Block

sealed interface TrainingLogEvent {
    object ShowNewBlockDialog: TrainingLogEvent
    object HideNewBlockDialog: TrainingLogEvent
    data class CreateBlock(val blockName: String): TrainingLogEvent
    data class DeleteBlock(val block: Block): TrainingLogEvent
}