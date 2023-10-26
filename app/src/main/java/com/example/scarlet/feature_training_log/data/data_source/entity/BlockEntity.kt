package com.example.scarlet.feature_training_log.data.data_source.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
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
    val id: Long = 0,
    val name: String = ""
) {

    constructor(block: Block) : this(
        id = block.id,
        name = block.name
    )

    fun toBlock() = Block(
        id = id,
        name = name
    )
}

data class BlockWithSessionsEntity(
    @Embedded
    val block: BlockEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "dayId",
        associateBy = Junction(
            value = DayEntity::class,
            parentColumn = "blockId",
            entityColumn = "id"
        )
    )
    val sessions: List<SessionEntity> = emptyList()
) {

    fun toModel() = BlockWithSessions(
        id = block.id,
        name = block.name,
        sessions = sessions.map { it.toModel() }
    )
}