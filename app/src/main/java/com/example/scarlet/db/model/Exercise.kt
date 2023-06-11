package com.example.scarlet.db.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

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
    val id: Int,
    val sessionId: Int,
    val movementId: Int,
    val order: Int
)
