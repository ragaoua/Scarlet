package com.example.scarlet.feature_training_log.data.data_source.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercisesWithMovementName
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

data class SessionWithExercisesWithMovementNameEntity(
    @Embedded
    val session: SessionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "sessionId"
    )
    val exercisesWithMovementName: List<ExerciseWithMovementNameEntity>
) {

    fun toSessionWithExercisesWithMovementName() = SessionWithExercisesWithMovementName(
        session = session.toSession(),
        exercises = exercisesWithMovementName.map { it.toExerciseWithMovementName() }
    )
}