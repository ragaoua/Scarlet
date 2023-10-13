package com.example.scarlet.feature_training_log.domain.model

import java.io.Serializable

interface IDay {
    val id: Long
    val blockId: Long
    val order: Int

    companion object Default {
        const val id: Long = 0
        const val blockId: Long = 0
        const val order: Int = 0
    }
}

data class Day(
    override val id: Long = IDay.id,
    override val blockId: Long = IDay.blockId,
    override val order: Int = IDay.order
): IDay, Serializable

data class DayWithSessions<T: ISession>(
    override val id: Long = IDay.id,
    override val blockId: Long = IDay.blockId,
    override val order: Int = IDay.order,
    val sessions: List<T> = emptyList()
): IDay {

    fun toDay() = Day(
        id = id,
        blockId = blockId,
        order = order
    )
}