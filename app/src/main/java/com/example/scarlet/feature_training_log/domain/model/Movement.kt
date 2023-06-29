package com.example.scarlet.feature_training_log.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movement(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = ""
)