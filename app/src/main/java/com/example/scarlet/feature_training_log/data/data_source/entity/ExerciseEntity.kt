package com.example.scarlet.feature_training_log.data.data_source.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.RatingType

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
    val order: Int = 0,
    @ColumnInfo(defaultValue = "RPE")
    val ratingType: RatingType = RatingType.RPE
) {

    constructor(exercise: Exercise) : this(
        id = exercise.id,
        sessionId = exercise.sessionId,
        movementId = exercise.movementId,
        order = exercise.order,
        ratingType = exercise.ratingType
    )

    fun toModel() = Exercise(
        id = id,
        sessionId = sessionId,
        movementId = movementId,
        order = order,
        ratingType = ratingType
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
        ratingType = exercise.ratingType,
        sets = sets.map { it.toSet() }
    )
}