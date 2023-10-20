package com.example.scarlet.feature_training_log.presentation.block

import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.RatingType
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.presentation.block.util.SetFieldType
import java.util.Date

sealed interface BlockEvent {
    object AddSession: BlockEvent
    data class DeleteSession(val session: Session) : BlockEvent
    data class UpdateSessionIndexScrollPosition(val index: Int): BlockEvent

    object EditBlock: BlockEvent
    object CancelBlockEdition: BlockEvent
    data class UpdateEditedBlockName(val editedBlockName: String) : BlockEvent
    object SaveBlockName: BlockEvent

    data class SelectDay(val day: Day) : BlockEvent

    data class ShowSessionDatePickerDialog(val session: Session) : BlockEvent
    object HideSessionDatePickerDialog : BlockEvent
    data class UpdateSessionDate(val date: Date) : BlockEvent

    object ToggleSessionEditMode : BlockEvent

    data class ToggleExerciseDetail(val exerciseId: Long) : BlockEvent
    data class ToggleSessionExercisesDetails(val sessionId: Long) : BlockEvent

    data class ShowExerciseDropdownMenu(val exerciseId: Long) : BlockEvent
    object DismissExerciseDropdownMenu : BlockEvent
    data class ShowMovementSelectionSheet(
        val sessionId: Long,
        val exercise: Exercise? = null
    ) : BlockEvent
    object HideMovementSelectionSheet : BlockEvent
    data class FilterMovementsByName(val nameFilter: String) : BlockEvent
    object AddMovement: BlockEvent
    data class SelectMovement(val movement: Movement) : BlockEvent
    data class EnableSupersetSelectionMode(val movement: Movement) : BlockEvent
    object DisableSupersetSelectionMode : BlockEvent
    object AddSuperset : BlockEvent

    data class ShowEditMovementSheet(val movement: Movement) : BlockEvent
    object HideEditMovementSheet: BlockEvent
    data class UpdateEditedMovementName(val editedMovementName: String) : BlockEvent
    object UpdateEditedMovement : BlockEvent
    object DeleteEditedMovement : BlockEvent

    data class DeleteExercise(val exercise: ExerciseWithMovementAndSets) : BlockEvent
    data class UpdateRatingType(
        val exercise: Exercise,
        val ratingType: RatingType
    ) : BlockEvent
    data class MoveExerciseUp(val exercise: Exercise) : BlockEvent
    data class MoveExerciseDown(val exercise: Exercise) : BlockEvent

    data class AddSet(val exercise: Exercise) : BlockEvent
    data class ShowSetTextField(val set: Set, val setFieldType: SetFieldType): BlockEvent
    object HideSetTextField: BlockEvent
    data class UpdateSetFieldValue(val value: String) : BlockEvent
    object UpdateSet : BlockEvent
    data class DeleteSet(val set: Set) : BlockEvent
    data class CopyPreviousSet(
        val set: Set,
        val fieldToCopy: SetFieldType
    ) : BlockEvent

    data class ShowLoadCalculationDialog(val set: Set) : BlockEvent
    object HideLoadCalculationDialog : BlockEvent
    data class UpdateLoadPercentage(val percentage: String) : BlockEvent
    data class UpdateCalculatedLoad(val load: Float) : BlockEvent
    object UpdateSetBasedOnPrecedingSet: BlockEvent

    object ShowFloatingActionButtons : BlockEvent
    object HideFloatingActionButtons : BlockEvent
}