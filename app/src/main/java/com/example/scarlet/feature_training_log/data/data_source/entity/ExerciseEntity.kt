package com.example.scarlet.feature_training_log.data.data_source.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovement
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets

@Entity(
    tableName = "exercise",
    indices = [
        Index("sessionId"),
        Index("movementId")
    ],
    foreignKeys = [
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("sessionId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MovementEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("movementId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: Long = 0,
    val movementId: Long = 0,
    val order: Int = 0
) {

    constructor(exercise: Exercise) : this(
        id = exercise.id,
        sessionId = exercise.sessionId,
        movementId = exercise.movementId,
        order = exercise.order
    )
}


data class ExerciseWithMovementAndSetsEntity(
    @Embedded
    val exercise: ExerciseEntity,
    @Relation(
        entity = MovementEntity::class,
        parentColumn = "movementId",
        entityColumn = "id"
    )
    val movement: MovementEntity,
    @Relation(
        entity = SetEntity::class,
        parentColumn = "id",
        entityColumn = "exerciseId"
    )
    val sets: List<SetEntity>
) {

    fun toModel() = ExerciseWithMovementAndSets(
        id = exercise.id,
        sessionId = exercise.sessionId,
        order = exercise.order,
        movementId = exercise.movementId,
        movement = movement.toMovement(),
        sets = sets.map { it.toSet() }
    )
}

data class ExerciseWithMovementEntity(
    @Embedded
    val exercise: ExerciseEntity,
    @Relation(
        entity = MovementEntity::class,
        parentColumn = "movementId",
        entityColumn = "id"
    )
    val movement: MovementEntity
) {
    fun toModel() = ExerciseWithMovement(
        id = exercise.id,
        sessionId = exercise.sessionId,
        order = exercise.order,
        movementId = exercise.movementId,
        movement = movement.toMovement()
    )
}