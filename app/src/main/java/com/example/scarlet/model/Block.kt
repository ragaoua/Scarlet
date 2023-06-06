package com.example.scarlet.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Block(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String = "",
    var completed: Boolean = false
) : Serializable {
}
