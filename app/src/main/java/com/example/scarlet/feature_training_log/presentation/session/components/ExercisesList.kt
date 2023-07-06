package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.presentation.session.SessionEvent

@Composable
fun ExercisesList(
    exercises: List<ExerciseWithMovementAndSets>,
    onEvent: (SessionEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(exercises) { exercise ->
            ExerciseHeader(exercise = exercise)
            ExerciseDetail(
                exercise = exercise,
                onEvent = onEvent
            )
        }
        item {
            NewExerciseButton(onEvent = onEvent)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoExercisePreview() {
    ExercisesList(
        exercises = emptyList(),
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ExercisesSectionPreview() {
    ExercisesList(
        exercises = listOf(
            ExerciseWithMovementAndSets(Exercise(), Movement(name="Squat"), emptyList()),
            ExerciseWithMovementAndSets(Exercise(), Movement(name="Bench"), emptyList()),
            ExerciseWithMovementAndSets(Exercise(), Movement(name="Deadlift"), emptyList())
        ),
        onEvent = {}
    )
}