package com.example.scarlet.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date

@Entity
data class Session(
    var blockId: Int? = null,
    var date: String = Date().toString()
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
