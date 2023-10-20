package com.example.scarlet.feature_training_log.presentation.block

import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.DayWithSessions
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercises
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.presentation.block.util.SetFieldType

data class BlockUiState (
    /** Block */
    val block: Block = Block(),
    val days: List<DayWithSessions<SessionWithExercises<ExerciseWithMovementAndSets>>> = emptyList(),
    val selectedDayId: Long = days.firstOrNull()?.id ?: 0,
    val editBlockSheet: EditBlockSheetState? = null, // null means the sheet is hidden

    val expandedDropdownMenuExerciseId: Long? = null, // null means no exercise dropdown menu is expanded

    /** Sessions */
    val sessionIndexScrollPositionByDayId: Map<Long, Int> = emptyMap(),
    val isInSessionEditMode: Boolean = false,
    val sessionDatePickerDialog: SessionDatePickerDialogState? = null, // null means the dialog is hidden

    /** Exercises */
    val isExerciseDetailExpandedById: Map<Long, Boolean> = emptyMap(),
    val movementSelectionSheet: MovementSelectionSheetState? = null, // null means the sheet is hidden
    val loadCalculationDialog: LoadCalculationDialogState? = null, // null means the dialog is hidden

    val setTextField: SetTextField? = null, // null means the text field is hidden

    val areFloatingActionButtonsVisible: Boolean = true
) {

    data class SessionDatePickerDialogState(val session: Session)

    data class EditBlockSheetState(
        val blockName: String = "",
        val blockNameError: StringResource? = null
    )

    data class MovementSelectionSheetState(
        val movements: List<Movement> = emptyList(),
        val sessionId: Long,
        val exercise: Exercise? = null,
        val movementNameFilter: String = "",
        val isInSupersetSelectionMode: Boolean = false,
        val supersetMovements: List<Movement> = emptyList(),
        val editMovementSheet: EditMovementSheetState? = null, // null means the sheet is hidden
    )

    data class EditMovementSheetState(
        val movement: Movement,
        val editedMovementName: String = movement.name,
        val movementNameError: StringResource? = null
    )

    data class LoadCalculationDialogState(
        val set: Set,
        val previousSetLoad: Float,
        val percentage: Int? = null, // 0-100
        val calculatedLoad: Float? = null
    )

    data class SetTextField(
        val set: Set,
        val value : String,
        val field: SetFieldType,
        val isLastField: Boolean = false
    )
}