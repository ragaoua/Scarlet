package com.example.scarlet.feature_training_log.domain.model

import java.io.Serializable

interface IExercise {
    val id: Long
    val sessionId: Long
    val order: Int
    val movementId: Long

    companion object Default {
        const val id: Long = 0
        const val sessionId: Long = 0
        const val order: Int = 0
        const val movementId: Long = 0
    }
}

data class Exercise(
    override val id: Long = IExercise.id,
    override val sessionId: Long = IExercise.sessionId,
    override val order: Int = IExercise.order,
    override val movementId: Long = IExercise.movementId
): IExercise, Serializable

data class ExerciseWithMovement(
    override val id: Long = IExercise.id,
    override val sessionId: Long = IExercise.sessionId,
    override val order: Int = IExercise.order,
    override val movementId: Long = IExercise.movementId,
    val movement: Movement
): IExercise

data class ExerciseWithMovementAndSets(
    override val id: Long = IExercise.id,
    override val sessionId: Long = IExercise.sessionId,
    override val order: Int = IExercise.order,
    override val movementId: Long = IExercise.movementId,
    val movement: Movement,
    val sets: List<Set>
): IExercise {

    fun toExercise() = Exercise(
        id = id,
        sessionId = sessionId,
        order = order,
        movementId = movementId
    )
}