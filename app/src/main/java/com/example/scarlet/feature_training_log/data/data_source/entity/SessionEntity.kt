package com.example.scarlet.feature_training_log.data.data_source.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.SessionWithMovements
import java.util.Date

@Entity(
    tableName = "session",
    indices = [
        Index("blockId")
    ],
    foreignKeys = [
        ForeignKey(
            entity = BlockEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("blockId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val blockId: Int = 0,
    val date: Date = Date(System.currentTimeMillis()),
) {

    constructor(session: Session) : this(
        id = session.id,
        blockId = session.blockId,
        date = session.date
    )

    fun toSession() = Session(
        id = id,
        blockId = blockId,
        date = date
    )
}

data class SessionWithMovementsEntity(
    @Embedded
    val session: SessionEntity,
    @Relation(
        parentColumn = "id",
        entity = MovementEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            value = ExerciseEntity::class,
            parentColumn = "sessionId",
            entityColumn = "movementId"
        )
    )
    val movements: List<MovementEntity>
) {

    fun toSessionWithMovements() = SessionWithMovements(
        session = session.toSession(),
        movements = movements.map { it.toMovement() }
    )
}