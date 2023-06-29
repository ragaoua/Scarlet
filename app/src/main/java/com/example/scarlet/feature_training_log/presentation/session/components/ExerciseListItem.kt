package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Set

@Composable
fun ExerciseListItem(
    modifier: Modifier = Modifier,
    exercise: ExerciseWithMovementAndSets
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            ExerciseHeader(exercise = exercise)
            ExerciseDetail(exercise = exercise)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview_noSets() {
    ExerciseListItem(
        exercise = ExerciseWithMovementAndSets(
            exercise = Exercise(),
            movement = Movement(name="Low-bar Squat"),
            sets = emptyList()
        )
    )
}

@Preview(showBackground = true)
@Composable
fun Preview_withSets() {
    ExerciseListItem(
        exercise = ExerciseWithMovementAndSets(
            exercise = Exercise(),
            movement = Movement(name="Low-bar Squat"),
            sets = listOf(
                Set(reps = 10, weight = 100f, rpe = 8f),
                Set(reps = 10, weight = 100f, rpe = 8.5f),
                Set(reps = 10, weight = 95f, rpe = 8f),
            )
        )
    )
}