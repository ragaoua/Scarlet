package com.example.scarlet.feature_training_log.domain.model

import java.io.Serializable
import java.util.Date

data class Session(
    val id: Int = 0,
    val blockId: Int = 0,
    val date: Date = Date(System.currentTimeMillis()),
): Serializable

data class SessionWithExercisesWithMovementName(
    val session: Session,
    val exercises: List<ExerciseWithMovementName>
)