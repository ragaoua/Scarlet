package com.example.scarlet.feature_training_log.data.data_source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.scarlet.feature_training_log.domain.model.Movement

@Entity(tableName = "movement")
data class MovementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = ""
) {

    constructor(movement: Movement) : this(
        id = movement.id,
        name = movement.name
    )

    fun toMovement() = Movement(
        id = id,
        name = name
    )
}