package com.example.scarlet.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val blockId: Int,
    val date: String = Date().toString()
)