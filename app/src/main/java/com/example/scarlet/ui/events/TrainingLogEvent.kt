package com.example.scarlet.ui.events

sealed interface TrainingLogEvent {
    object ShowNewBlockDialog: TrainingLogEvent
    object HideNewBlockDialog: TrainingLogEvent
    data class CreateBlock(val blockName: String): TrainingLogEvent
}