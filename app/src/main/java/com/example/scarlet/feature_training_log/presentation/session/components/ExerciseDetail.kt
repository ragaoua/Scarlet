package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.presentation.session.SessionEvent

@Composable
fun ExerciseDetail(
    modifier: Modifier = Modifier,
    exercise: ExerciseWithMovementAndSets,
    onEvent: (SessionEvent) -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface))
                .padding(8.dp)
        ) {
            ExerciseDetailHeader(
                modifier = Modifier.fillMaxWidth()
            )
            exercise.sets.forEach { set ->
                ExerciseSetRow(set)
            }
            AddSetButton(
                exercise = exercise.exercise,
                onEvent = onEvent
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewExerciseDetail_noSets() {
    ExerciseDetail(
        exercise = ExerciseWithMovementAndSets(
            exercise = Exercise(),
            movement = Movement(),
            sets = emptyList()
        ),
        onEvent = {}
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewExerciseDetail_withSets() {
    ExerciseDetail(
        exercise = ExerciseWithMovementAndSets(
            exercise = Exercise(),
            movement = Movement(),
            sets = listOf(
                Set(order = 1, reps = 10, weight = 100f, rpe = 8f),
                Set(order = 2, reps = 10, weight = 100f, rpe = 8.5f),
                Set(order = 3, reps = 10, weight = 95f, rpe = 8f)
            )
        ),
        onEvent = {}
    )
}