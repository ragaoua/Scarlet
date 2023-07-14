package com.example.scarlet.feature_training_log.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.scarlet.feature_training_log.presentation.core.DateFormatter
import java.io.Serializable
import java.time.LocalDate

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
    val date: String = LocalDate.now().format(DateFormatter),
): Serializable