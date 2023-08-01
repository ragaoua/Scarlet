package com.example.scarlet.feature_training_log.presentation.session

import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.Set

data class SessionUiState (
    val session: Session = Session(),
    val sessionBlockName: String = "",
    val exercises: List<ExerciseWithMovementAndSets> = emptyList(),
    val movements: List<Movement> = emptyList(),
    val isMovementSelectionSheetOpen: Boolean = false,
    val isDatePickerDialogOpen: Boolean = false,
    val isInEditMode: Boolean = false,
    val exerciseToEdit: Exercise? = null,
    val loadCalculationDialogState: LoadCalculationDialogState? = null
) {
    data class LoadCalculationDialogState(
        val set: Set,
        val previousSet: Set
    )
}