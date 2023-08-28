package com.example.scarlet.feature_training_log.domain.model

import java.io.Serializable

interface IDay {
    val id: Long
    val blockId: Long
    val name: String
    val order: Int

    companion object Default {
        const val id: Long = 0
        const val blockId: Long = 0
        const val name: String = ""
        const val order: Int = 0
    }
}

data class Day(
    override val id: Long = IDay.id,
    override val blockId: Long = IDay.blockId,
    override val name: String = IDay.name,
    override val order: Int = IDay.order
): IDay, Serializable

data class DayWithSessions<T: ISession>(
    override val id: Long = IDay.id,
    override val blockId: Long = IDay.blockId,
    override val name: String = IDay.name,
    override val order: Int = IDay.order,
    val sessions: List<T> = emptyList()
): IDay

data class DayWithSessionsWithExercisesWithMovement(
    val day: Day = Day(),
    val sessions: List<SessionWithExercisesWithMovement> = emptyList()
)