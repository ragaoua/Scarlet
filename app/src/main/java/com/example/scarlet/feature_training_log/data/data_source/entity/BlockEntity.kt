package com.example.scarlet.feature_training_log.data.data_source.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.BlockWithList

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

data class BlockWithDaysWithSessionsEntity(
    @Embedded
    val block: BlockEntity,
    @Relation(
        entity = DayEntity::class,
        parentColumn = "id",
        entityColumn = "blockId"
    )
    val days: List<DayWithSessionsEntity> = emptyList()
) {

    fun toModel() = BlockWithList(
        id = block.id,
        name = block.name,
        days = days.map { it.toModel() }
    )
}