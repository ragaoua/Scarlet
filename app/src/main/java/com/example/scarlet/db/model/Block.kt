package com.example.scarlet.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Block(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String = "",
    var completed: Boolean = false
)
