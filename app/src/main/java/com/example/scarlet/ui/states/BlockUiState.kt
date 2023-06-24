package com.example.scarlet.ui.states

import com.example.scarlet.db.model.Block
import com.example.scarlet.db.model.Session

data class BlockUiState (
    val block: Block = Block(),
    val sessions: List<Session> = emptyList()
)