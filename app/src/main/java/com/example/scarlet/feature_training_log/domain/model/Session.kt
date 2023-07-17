package com.example.scarlet.feature_training_log.domain.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
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

data class SessionWithMovements(
    @Embedded
    val session: Session,
    @Relation(
        parentColumn = "id",
        entity = Movement::class,
        entityColumn = "id",
        associateBy = Junction(
            value = Exercise::class,
            parentColumn = "sessionId",
            entityColumn = "movementId"
        )
    )
    val movements: List<Movement>
)