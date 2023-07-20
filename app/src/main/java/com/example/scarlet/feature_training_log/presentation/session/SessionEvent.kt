package com.example.scarlet.feature_training_log.presentation.session

import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.Set
import java.util.Date

sealed interface SessionEvent {

    object OpenDatePickerDialog : SessionEvent
    object CloseDatePickerDialog : SessionEvent
    data class UpdateSessionDate(val date: Date) : SessionEvent

    data class FilterMovementsByName(val nameFilter: String) : SessionEvent

    object ToggleEditMode : SessionEvent

    object ExpandMovementSelectionSheet : SessionEvent
    object CollapseMovementSelectionSheet : SessionEvent
    data class NewExercise(val movementId: Int) : SessionEvent
    data class DeleteExercise(val exercise: Exercise) : SessionEvent

    data class NewSet(val exercise: Exercise) : SessionEvent
    data class UpdateSet(val set: Set) : SessionEvent
    data class DeleteSet(val set: Set) : SessionEvent
}