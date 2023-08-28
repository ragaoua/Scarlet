package com.example.scarlet.feature_training_log.domain.model

import java.io.Serializable

data class Day(
    val id: Long = 0,
    val blockId: Long = 0,
    val name: String = "",
    val order: Int = 0
): Serializable

data class DayWithSessionsWithExercisesWithMovementName(
    val day: Day = Day(),
    val sessions: List<SessionWithExercisesWithMovement> = emptyList()
)