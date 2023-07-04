package com.example.scarlet.feature_training_log.presentation.session

import com.example.scarlet.feature_training_log.domain.model.Exercise

sealed interface SessionEvent {
    data class NewSet(val exercise: Exercise) : SessionEvent
    /* TODO */
}