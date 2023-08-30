package com.example.scarlet.feature_training_log.presentation.block

import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.model.DayWithSessions
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.IExercise
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercises
import com.example.scarlet.feature_training_log.domain.model.Set

data class BlockUiState (
    val block: Block = Block(),
    val days: List<DayWithSessions<SessionWithExercises<ExerciseWithMovementAndSets>>> = emptyList(),
    val selectedDay: Day? = null,

    val isInSessionEditMode: Boolean = false,

    val sessionDatePickerDialog: SessionDatePickerDialogState? = null, // null means the dialog is hidden

    val editBlockSheet: EditBlockSheetState? = null, // null means the sheet is hidden

    val movements: List<Movement> = emptyList(),
    val movementSelectionSheet: MovementSelectionSheetState? = null, // null means the sheet is hidden

    val loadCalculationDialog: LoadCalculationDialogState? = null // null means the dialog is hidden
) {

    data class SessionDatePickerDialogState(val session: Session)

    data class EditBlockSheetState(
        val blockName: String = "",
        val blockNameError: StringResource? = null,
        val areMicroCycleSettingsExpanded: Boolean = false,
        val daysPerMicroCycle: Int = 3
    )

    data class MovementSelectionSheetState(
        val session: SessionWithExercises<out IExercise>,
        val exercise: Exercise? = null,
        val movementNameFilter: String = ""
    )

    data class LoadCalculationDialogState(
        val set: Set,
        val previousSet: Set
    )
}