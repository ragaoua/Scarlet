package com.example.scarlet.feature_training_log.domain.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable

@Entity(
    indices = [
        Index("sessionId"),
        Index("movementId")
    ],
    foreignKeys = [
        ForeignKey(
            entity = Session::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("sessionId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Movement::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("movementId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sessionId: Int = 0,
    val movementId: Int = 0,
    val order: Int = 0
): Serializable


data class ExerciseWithMovementAndSets(
    @Embedded(prefix = "exercise_")
    val exercise: Exercise,
    @Embedded(prefix = "movement_")
    val movement: Movement,
    @Relation(
        entity = Set::class,
        parentColumn = "exercise_id",
        entityColumn = "exerciseId"
    )
    val sets: List<Set>
)