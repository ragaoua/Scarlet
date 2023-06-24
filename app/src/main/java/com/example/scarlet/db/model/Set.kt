package com.example.scarlet.db.model

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
    val reps: Int = 0,
    val weight: Float = 0f,
    val rpe: Float? = null
) : Serializable