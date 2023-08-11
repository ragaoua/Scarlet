package com.example.scarlet.feature_training_log.data.data_source.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.BlockWithSessions

@Entity(
    tableName = "block",
    indices = [
        Index(value = ["name"], unique = true)
    ]
)
data class BlockEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val completed: Boolean = false
) {

    constructor(block: Block) : this(
        id = block.id,
        name = block.name,
        completed = block.completed
    )

    fun toBlock() = Block(
        id = id,
        name = name,
        completed = completed
    )
}

data class BlockWithSessionsEntity(
    @Embedded
    val block: BlockEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "blockId"
    )
    val sessions: List<SessionEntity> = emptyList()
) {

    fun toBlockWithSessions() = BlockWithSessions(
        block = block.toBlock(),
        sessions = sessions.map { it.toSession() }
    )
}