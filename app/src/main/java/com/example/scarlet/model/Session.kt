package com.example.scarlet.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date

@Entity
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val blockId: Int? = null,
    val date: String = Date().toString()
) : Serializable
