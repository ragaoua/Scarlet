package com.example.scarlet.feature_training_log.domain.model

import java.io.Serializable

interface IBlock {
    val id: Long
    val name: String

    companion object Default {
        const val id: Long = 0
        const val name: String = ""
    }
}

data class Block(
    override val id: Long = IBlock.id,
    override val name: String = IBlock.name
): IBlock, Serializable

data class BlockWithDays<T: IDay>(
    override val id: Long = IBlock.id,
    override val name: String = IBlock.name,
    val days: List<T> = emptyList()
): IBlock {

    fun toBlock() = Block(
        id = id,
        name = name
    )
}