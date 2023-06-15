package com.example.scarlet.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Block(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String = "",
    var completed: Boolean = false
): Serializable