package com.example.scarlet.feature_training_log.presentation.training_log

import com.example.scarlet.feature_training_log.domain.model.Block

sealed interface TrainingLogViewModelUiEvent {
    data class NavigateToBlockScreen(val block: Block): TrainingLogViewModelUiEvent
}