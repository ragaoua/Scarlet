package com.example.scarlet.feature_training_log.data.data_source.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.scarlet.feature_training_log.domain.model.Day

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