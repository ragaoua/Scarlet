package com.example.scarlet.feature_training_log.data.data_source.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.scarlet.feature_training_log.domain.model.Day
import com.example.scarlet.feature_training_log.domain.model.DayWithSessions

@Entity(
    tableName = "day",
    indices = [
        Index("blockId"),
        Index(value = ["blockId", "order"], unique = true)
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
data class DayEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val blockId: Long = 0,
    val name: String = "",
    val order: Int = 0
) {

    constructor(day: Day) : this(
        id = day.id,
        blockId = day.blockId,
        name = day.name,
        order = day.order
    )

    fun toDay() = Day(
        id = id,
        blockId = blockId,
        name = name,
        order = order
    )
}

data class DayWithSessionsEntity(
    @Embedded
    val day: DayEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "dayId"
    )
    val sessions: List<SessionEntity> = emptyList()
) {

    fun toModel() = DayWithSessions(
        id = day.id,
        blockId = day.blockId,
        name = day.name,
        order = day.order,
        sessions = sessions.map { it.toModel() }
    )
}


data class DayWithSessionsWithExercisesWithMovementAndSetsEntity(
    @Embedded
    val day: DayEntity,
    @Relation(
        entity = SessionEntity::class,
        parentColumn = "id",
        entityColumn = "dayId"
    )
    val sessionsWithExercisesWithMovementAndSets: List<SessionWithExercisesWithMovementAndSetsEntity>
) {

    fun toModel() = DayWithSessions(
        id = day.id,
        blockId = day.blockId,
        name = day.name,
        order = day.order,
        sessions = sessionsWithExercisesWithMovementAndSets.map { it.toModel() }
    )
}