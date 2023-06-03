package com.example.scarlet.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Block(
    var name: String? = null,
    var completed: Boolean = false
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
