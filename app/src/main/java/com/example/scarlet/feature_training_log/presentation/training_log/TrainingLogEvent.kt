package com.example.scarlet.feature_training_log.presentation.training_log

import com.example.scarlet.feature_training_log.domain.model.Block

sealed interface TrainingLogEvent {
    object ShowNewBlockSheet: TrainingLogEvent
    object HideNewBlockSheet: TrainingLogEvent
    data class UpdateNewBlockName(val newBlockName: String): TrainingLogEvent
    object ToggleMicroCycleSettings : TrainingLogEvent
    class UpdateDaysPerMicroCycle(val nbDays: Int) : TrainingLogEvent
    data class AddBlock(val blockName: String): TrainingLogEvent
    data class DeleteBlock(val block: Block): TrainingLogEvent
}