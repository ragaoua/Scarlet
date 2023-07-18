package com.example.scarlet.feature_training_log.presentation.session

import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.Set

sealed interface SessionEvent {

    object OpenDatePickerDialog : SessionEvent
    object CloseDatePickerDialog : SessionEvent
    data class SaveSession(val session: Session) : SessionEvent

    object ShowNewExerciseDialog : SessionEvent
    object HideNewExerciseDialog : SessionEvent
    data class NewExercise(val movementId: Int) : SessionEvent
    data class DeleteExercise(val exercise: Exercise) : SessionEvent

    data class NewSet(val exercise: Exercise) : SessionEvent
    data class UpdateSet(val set: Set) : SessionEvent
    data class DeleteSet(val set: Set) : SessionEvent
}