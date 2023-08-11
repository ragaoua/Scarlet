package com.example.scarlet.feature_training_log.domain.model

import java.io.Serializable

data class Exercise(
    val id: Long = 0,
    val sessionId: Long = 0,
    val movementId: Long = 0,
    val order: Int = 0
): Serializable


data class ExerciseWithMovementAndSets(
    val exercise: Exercise,
    val movement: Movement,
    val sets: List<Set>
)

data class ExerciseWithMovementName(
    val exercise: Exercise,
    val movementName: String
)