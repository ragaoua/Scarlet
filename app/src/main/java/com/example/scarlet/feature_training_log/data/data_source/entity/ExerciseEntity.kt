package com.example.scarlet.feature_training_log.data.data_source.entity

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementName

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
    val id: Int = 0,
    val sessionId: Int = 0,
    val movementId: Int = 0,
    val order: Int = 0
) {

    constructor(exercise: Exercise) : this(
        id = exercise.id,
        sessionId = exercise.sessionId,
        movementId = exercise.movementId,
        order = exercise.order
    )

    fun toExercise() = Exercise(
        id = id,
        sessionId = sessionId,
        movementId = movementId,
        order = order
    )
}


data class ExerciseWithMovementAndSetsEntity(
    @Embedded(prefix = "exercise_")
    val exercise: ExerciseEntity,
    @Embedded(prefix = "movement_")
    val movement: MovementEntity,
    @Relation(
        entity = SetEntity::class,
        parentColumn = "exercise_id",
        entityColumn = "exerciseId"
    )
    val sets: List<SetEntity>
) {

    fun toExerciseWithMovementAndSets() = ExerciseWithMovementAndSets(
        exercise = exercise.toExercise(),
        movement = movement.toMovement(),
        sets = sets.map { it.toSet() }
    )
}

@DatabaseView(
    """
        SELECT exercise.*, movement.name as movementName
        FROM exercise
        INNER JOIN movement ON exercise.movementId = movement.id
    """
)
data class ExerciseWithMovementNameEntity(
    @Embedded
    val exercise: ExerciseEntity,
    val movementName: String
) {
    fun toExerciseWithMovementName() = ExerciseWithMovementName(
        exercise = exercise.toExercise(),
        movementName = movementName
    )
}