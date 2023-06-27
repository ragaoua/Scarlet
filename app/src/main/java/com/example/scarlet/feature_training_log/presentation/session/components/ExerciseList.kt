package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.presentation.destinations.ExerciseScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator


@Composable
fun ExercisesList(
    exercises: List<Exercise>,
    navigator: DestinationsNavigator
) {
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        exercises.forEach { exercise ->
            Button(onClick = {
                navigator.navigate(ExerciseScreenDestination(exercise = exercise))
            }) {
                Text(exercise.movementId.toString())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoExercisePreview() {
    ExercisesList(
        navigator = EmptyDestinationsNavigator,
        exercises = emptyList()
    )
}

@Preview(showBackground = true)
@Composable
fun ExercisesSectionPreview() {
    ExercisesList(
        navigator = EmptyDestinationsNavigator,
        exercises = listOf(
            Exercise(movementId = 1),
            Exercise(movementId = 2),
            Exercise(movementId = 3)
        )
    )
}