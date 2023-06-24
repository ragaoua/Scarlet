package com.example.scarlet.ui.states

import com.example.scarlet.db.model.Exercise
import com.example.scarlet.db.model.Session

data class SessionUiState (
    val session: Session = Session(),
    val exercises: List<Exercise> = emptyList()
)