package com.example.scarlet.feature_training_log.data.data_source.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercises
import java.util.Date

@Entity(
    tableName = "session",
    indices = [
        Index("dayId")
    ],
    foreignKeys = [
        ForeignKey(
            entity = DayEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("dayId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dayId: Long = 0,
    val date: Date = Date(System.currentTimeMillis()),
) {

    constructor(session: Session) : this(
        id = session.id,
        dayId = session.dayId,
        date = session.date
    )

    fun toSession() = Session(
        id = id,
        dayId = dayId,
        date = date
    )
}

data class SessionWithExercisesWithMovementEntity(
    @Embedded
    val session: SessionEntity,
    @Relation(
        entity = ExerciseEntity::class,
        parentColumn = "id",
        entityColumn = "sessionId"
    )
    val exercisesWithMovement: List<ExerciseWithMovementEntity>
) {

    fun toModel() = SessionWithExercises(
        id = session.id,
        dayId = session.dayId,
        date = session.date,
        exercises = exercisesWithMovement.map { it.toModel() }
    )
}