package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.IExercise
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercises
import com.example.scarlet.feature_training_log.presentation.block.BlockEvent

@Composable
fun Superset(
    modifier: Modifier = Modifier,
    session: SessionWithExercises<out IExercise>,
    exercises: List<ExerciseWithMovementAndSets>,
    isExerciseDetailExpandedById: Map<Long, Boolean>,
    isInSessionEditMode: Boolean,
    onEvent: (BlockEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = MaterialTheme.shapes.small
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        exercises.forEach { exercise ->
            Exercise(
                session = session,
                exercise = exercise,
                isExerciseDetailExpandedById = isExerciseDetailExpandedById,
                isInSessionEditMode = isInSessionEditMode,
                onEvent = onEvent
            )
        }
    }
}