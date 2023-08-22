package com.example.scarlet.feature_training_log.presentation.training_log

import com.example.scarlet.feature_training_log.domain.model.Block

sealed interface TrainingLogEvent {
    object ToggleNewBlockSheet: TrainingLogEvent
    data class UpdateNewBlockName(val newBlockName: String): TrainingLogEvent
    data class AddBlock(val blockName: String): TrainingLogEvent
    data class DeleteBlock(val block: Block): TrainingLogEvent
}