package com.example.scarlet.feature_training_log.domain.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Block(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String = "",
    var completed: Boolean = false
): Serializable

data class BlockWithDates(
    @Embedded val block: Block,
    val firstSessionDate: String? = null,
    val lastSessionDate: String? = null
): Serializable