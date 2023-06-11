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
    val id: Int,
    val exerciseId: Int,
    val order: Int,
    val reps: Int,
    val weight: Float,
    val rpe: Float? = null
) : Serializable