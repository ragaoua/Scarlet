package com.example.scarlet.feature_training_log.presentation.block

import com.example.scarlet.feature_training_log.domain.model.Session

sealed interface BlockViewModelUiEvent {
    object NavigateUp: BlockViewModelUiEvent
    data class NavigateToSessionScreen(val session: Session): BlockViewModelUiEvent
}