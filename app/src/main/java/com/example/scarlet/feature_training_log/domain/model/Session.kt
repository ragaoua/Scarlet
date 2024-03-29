package com.example.scarlet.feature_training_log.domain.model

import java.io.Serializable
import java.util.Date

interface ISession {
    val id: Long
    val dayId: Long
    val date: Date

    companion object Default {
        const val id: Long = 0
        const val dayId: Long = 0
        val date: Date = Date(System.currentTimeMillis())
    }
}

data class Session(
    override val id: Long = ISession.id,
    override val dayId: Long = ISession.dayId,
    override val date: Date = ISession.date,
): ISession, Serializable

data class SessionWithExercises<T: IExercise>(
    override val id: Long = ISession.id,
    override val dayId: Long = ISession.dayId,
    override val date: Date = ISession.date,
    val exercises: List<T>
): ISession {

    constructor(session: Session, exercises: List<T>): this(
        id = session.id,
        dayId = session.dayId,
        date = session.date,
        exercises = exercises
    )

    fun toSession() = Session(
        id = id,
        dayId = dayId,
        date = date
    )
}