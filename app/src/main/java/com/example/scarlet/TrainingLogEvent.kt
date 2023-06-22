package com.example.scarlet

sealed interface TrainingLogEvent {
    object ShowNewBlockDialog: TrainingLogEvent
    object HideNewBlockDialog: TrainingLogEvent
    data class CreateBlock(val blockName: String): TrainingLogEvent
}