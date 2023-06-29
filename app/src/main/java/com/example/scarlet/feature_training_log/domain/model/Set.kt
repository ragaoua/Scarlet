package com.example.scarlet.feature_training_log.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    indices = [
        Index("exerciseId")
    ],
    foreignKeys = [
        ForeignKey(
            entity = Exercise::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("exerciseId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Set(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val exerciseId: Int = 0,
    val order: Int = 0,
    val reps: Int? = null,
    val weight: Float? = null,
    val rpe: Float? = null
) : Serializable