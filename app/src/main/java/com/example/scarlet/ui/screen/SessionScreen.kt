package com.example.scarlet.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scarlet.db.model.Exercise
import com.example.scarlet.db.model.Session
import com.example.scarlet.ui.screen.destinations.ExerciseScreenDestination
import com.example.scarlet.ui.theme.ScarletTheme
import com.example.scarlet.viewmodel.TrainingLogViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun SessionScreen(
    navigator: DestinationsNavigator,
    session: Session
) {
    val trainingLogViewModel: TrainingLogViewModel = hiltViewModel()

    val sessionExercises by trainingLogViewModel.getExercisesBySessionId(session.id).collectAsState(initial = emptyList())
    ScarletTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SessionHeader(
                sessionDate = session.date
            )
            ExercisesSection(
                exercises = sessionExercises,
                navigator = navigator
            )
        }
    }
}


@Composable
fun SessionHeader(
    sessionDate: String
) {
    Text(
        text = sessionDate,
        fontSize = 20.sp
    )
}

@Composable
fun ExercisesSection(
    exercises: List<Exercise>,
    navigator: DestinationsNavigator
) {
    Column (
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