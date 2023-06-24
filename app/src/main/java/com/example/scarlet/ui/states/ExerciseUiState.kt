package com.example.scarlet.ui.states

import com.example.scarlet.db.model.Exercise
import com.example.scarlet.db.model.Set

data class ExerciseUiState (
    val exercise: Exercise = Exercise(),
    val sets: List<Set> = emptyList()
)