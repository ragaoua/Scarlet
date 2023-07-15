package com.example.scarlet.feature_training_log.presentation.training_log

import com.example.scarlet.feature_training_log.domain.model.Block

sealed interface TrainingLogEvent {
    object ShowNewBlockSheet: TrainingLogEvent
    object HideNewBlockSheet: TrainingLogEvent
    data class CreateBlock(val blockName: String): TrainingLogEvent
    data class DeleteBlock(val block: Block): TrainingLogEvent
}