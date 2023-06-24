package com.example.scarlet.db.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    val id: Int = 0,
    val blockId: Int = 0,
    val date: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
): Serializable