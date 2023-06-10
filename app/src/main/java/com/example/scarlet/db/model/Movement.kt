package com.example.scarlet.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movement(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)