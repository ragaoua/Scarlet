package com.example.scarlet.feature_training_log.presentation.session

import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Session

data class SessionUiState (
    val session: Session = Session(),
    val exercises: List<ExerciseWithMovementAndSets> = emptyList(),
    val movements: List<Movement> = emptyList(),
    val isAddingExercise: Boolean = false
)