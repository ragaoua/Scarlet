package com.example.scarlet.feature_training_log.data.data_source.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.scarlet.feature_training_log.domain.model.Set

@Entity(
    tableName = "set",
    indices = [
        Index("exerciseId")
    ],
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("exerciseId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val exerciseId: Int = 0,
    val order: Int = 0,
    val reps: Int? = null,
    val weight: Float? = null,
    val rpe: Float? = null
) {

    constructor(set: Set) : this(
        id = set.id,
        exerciseId = set.exerciseId,
        order = set.order,
        reps = set.reps,
        weight = set.weight,
        rpe = set.rpe
    )

    fun toSet() = Set(
        id = id,
        exerciseId = exerciseId,
        order = order,
        reps = reps,
        weight = weight,
        rpe = rpe
    )
}