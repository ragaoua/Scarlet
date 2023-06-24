package com.example.scarlet.events

sealed interface TrainingLogEvent {
    object ShowNewBlockDialog: TrainingLogEvent
    object HideNewBlockDialog: TrainingLogEvent
    data class CreateBlock(val blockName: String): TrainingLogEvent
}