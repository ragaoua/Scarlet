package com.example.scarlet.db.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    indices = [
        Index("blockId")
    ],
    foreignKeys = [
        ForeignKey(
            entity = Block::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("blockId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val blockId: Int,
    val date: String = Date().toString()
)