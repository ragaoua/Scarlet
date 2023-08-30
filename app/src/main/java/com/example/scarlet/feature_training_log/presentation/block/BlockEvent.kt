package com.example.scarlet.feature_training_log.presentation.block

import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.IExercise
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercises
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.presentation.block.util.SetFieldType
import java.util.Date

sealed interface BlockEvent {
    object AddSession: BlockEvent
    data class DeleteSession(val session: Session) : BlockEvent

    object EditBlock: BlockEvent
    object CancelBlockEdition: BlockEvent
    data class UpdateEditedBlockName(val editedBlockName: String) : BlockEvent
    object ToggleMicroCycleSettings: BlockEvent
    data class UpdateDaysPerMicroCycle(val nbDays: Int) : BlockEvent
    object SaveBlockName: BlockEvent

    data class SelectDay(val day: Day) : BlockEvent

    data class ShowSessionDatePickerDialog(val session: Session) : BlockEvent
    object HideSessionDatePickerDialog : BlockEvent
    data class UpdateSessionDate(val date: Date) : BlockEvent

    object ToggleSessionEditMode : BlockEvent

    data class ShowMovementSelectionSheet(
        val session: SessionWithExercises<out IExercise>,
        val exercise: Exercise? = null
    ) : BlockEvent
    object HideMovementSelectionSheet : BlockEvent
    data class FilterMovementsByName(val nameFilter: String) : BlockEvent
    object AddMovement: BlockEvent
    data class SelectMovement(val movement: Movement) : BlockEvent
    data class DeleteExercise(val exercise: Exercise) : BlockEvent

    data class AddSet(val exercise: Exercise) : BlockEvent
    data class UpdateSet(val set: Set) : BlockEvent
    data class DeleteSet(val set: Set) : BlockEvent
    data class CopyPreviousSet(
        val set: Set,
        val fieldToCopy: SetFieldType
    ) : BlockEvent

    data class ShowLoadCalculationDialog(val set: Set) : BlockEvent
    object HideLoadCalculationDialog : BlockEvent
    data class CalculateLoad(val percentage: String) : BlockEvent
}