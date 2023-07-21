package com.example.scarlet.feature_training_log.domain.model

import java.io.Serializable

data class Block(
    val id: Int = 0,
    var name: String = "",
    var completed: Boolean = false
): Serializable

data class BlockWithSessions(
    val block: Block,
    val sessions: List<Session> = emptyList()
): Serializable