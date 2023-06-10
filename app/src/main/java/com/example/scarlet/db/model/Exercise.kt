package com.example.scarlet.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val sessionId: Int,
    val movementId: Int,
    val order: Int
)
