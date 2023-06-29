package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Set

@Composable
fun ExerciseDetail(
    modifier: Modifier = Modifier,
    exercise: ExerciseWithMovementAndSets
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface))
        ) {
            ExerciseDetailHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                items(exercise.sets) { set ->
                    ExerciseSetRow(set)
                }
                /* TODO implement the "add set" button */
            }
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
        )
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
                Set(reps = 10, weight = 100f, rpe = 8f),
                Set(reps = 10, weight = 100f, rpe = 8.5f),
                Set(reps = 10, weight = 95f, rpe = 8f),
            )
        )
    )
}