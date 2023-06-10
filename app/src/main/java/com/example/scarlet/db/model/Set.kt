package com.example.scarlet.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Set(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val exerciseId: Int,
    val reps: Int,
    val weight: Float,
    val rpe: Float? = null
) : Serializable