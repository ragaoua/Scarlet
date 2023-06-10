package com.example.scarlet.compose

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scarlet.Screen
import com.example.scarlet.model.Exercise
import com.example.scarlet.theme.ScarletTheme
import com.example.scarlet.viewmodel.TrainingLogViewModel
import com.example.scarlet.viewmodel.TrainingLogViewModelFactory

@Composable
fun SessionScreen(
    sessionId: Int,
    navController: NavController,
    factory: TrainingLogViewModelFactory,
    trainingLogViewModel: TrainingLogViewModel = viewModel(factory = factory)
) {
    val session by trainingLogViewModel.getSessionById(sessionId).collectAsState(initial = null)
    val sessionExercises by trainingLogViewModel.getExercisesBySessionId(sessionId).collectAsState(initial = emptyList())
    ScarletTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            session?.let {
                SessionHeader(
                    sessionDate = session!!.date,
                    navController = navController,
                    factory = factory
                )
                ExercisesSection(
                    exercises = sessionExercises,
                    navController = navController,
                    factory = factory
                )
            }
        }
    }
}


@Composable
fun SessionHeader(
    sessionDate: String,
    navController: NavController,
    factory: TrainingLogViewModelFactory,
    trainingLogViewModel: TrainingLogViewModel = viewModel(factory = factory)
) {
    Text(
        text = sessionDate,
        fontSize = 20.sp
    )
}

@Composable
fun ExercisesSection(
    exercises: List<Exercise>,
    navController: NavController,
    factory: TrainingLogViewModelFactory,
    trainingLogViewModel: TrainingLogViewModel = viewModel(factory = factory)
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        exercises.forEach { exercise ->
            Button(onClick = {
                navController.navigate(Screen.SessionScreen.withId(exercise.id))
            }) {
                Text(exercise.movementId.toString())
            }
        }
    }
}