package com.example.scarlet.feature_training_log.presentation.block

import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.model.DayWithSessions
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercises

data class BlockUiState (
    val block: Block = Block(),
    val days: List<DayWithSessions<SessionWithExercises<ExerciseWithMovementAndSets>>> = emptyList(),
    val selectedDay: Day? = null,

    val editBlockSheetState: EditBlockSheetState? = null, // null means the sheet is hidden

    val movements: List<Movement> = emptyList(),
    val movementSelectionSheetState: MovementSelectionSheetState? = null // null means the sheet is hidden
) {

    data class EditBlockSheetState(
        val blockName: String = "",
        val blockNameError: StringResource? = null,
        val areMicroCycleSettingsExpanded: Boolean = false,
        val daysPerMicroCycle: Int = 3
    )

    data class MovementSelectionSheetState(
        val session: Session,
        val exercise: Exercise? = null
    )
}