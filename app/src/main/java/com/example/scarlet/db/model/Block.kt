package com.example.scarlet.db.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable

@Entity
data class Block(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String = "",
    var completed: Boolean = false
): Serializable

data class BlockWithSessions(
    @Embedded val block: Block,
    @Relation(
        parentColumn = "id",
        entityColumn = "blockId"
    )
    val sessions: List<Session>
)