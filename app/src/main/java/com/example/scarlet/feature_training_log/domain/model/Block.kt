package com.example.scarlet.feature_training_log.domain.model

import java.io.Serializable

data class Block(
    val id: Long = 0,
    val name: String = ""
): Serializable

data class BlockWithSessions(
    val block: Block,
    val sessions: List<Session> = emptyList()
): Serializable