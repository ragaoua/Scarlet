package com.example.scarlet.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Block(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val completed: Boolean = false
) : Serializable
