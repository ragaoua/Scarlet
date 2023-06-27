package com.example.scarlet.feature_training_log.presentation.exercise

import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.Set

data class ExerciseUiState (
    val exercise: Exercise = Exercise(),
    val sets: List<Set> = emptyList()
)