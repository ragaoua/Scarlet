package com.example.scarlet.feature_training_log.presentation.session

import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.Set
import java.util.Date

sealed interface SessionEvent {

    object ToggleDatePickerDialog : SessionEvent
    data class UpdateSessionDate(val date: Date) : SessionEvent

    object ToggleEditMode : SessionEvent

    data class ShowMovementSelectionSheet(val exercise: Exercise? = null) : SessionEvent
    object HideMovementSelectionSheet : SessionEvent
    data class FilterMovementsByName(val nameFilter: String) : SessionEvent
    object ToggleNewMovementSheet : SessionEvent
    data class AddMovement(val name: String) : SessionEvent

    data class SelectMovement(val movementId: Int) : SessionEvent
    data class DeleteExercise(val exercise: Exercise) : SessionEvent

    data class AddSet(val exercise: Exercise) : SessionEvent
    data class UpdateSet(val set: Set) : SessionEvent
    data class DeleteSet(val set: Set) : SessionEvent
}