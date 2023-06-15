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
import androidx.navigation.NavController
import com.example.scarlet.ui.navigation.Screen
import com.example.scarlet.db.model.Exercise
import com.example.scarlet.ui.theme.ScarletTheme
import com.example.scarlet.viewmodel.TrainingLogViewModel

@Composable
fun SessionScreen(
    sessionId: Int,
    navController: NavController,
    trainingLogViewModel: TrainingLogViewModel
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
                    sessionDate = session!!.date
                )
                ExercisesSection(
                    exercises = sessionExercises,
                    navController = navController
                )
            }
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
    navController: NavController
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        exercises.forEach { exercise ->
            Button(onClick = {
                navController.navigate(Screen.ExerciseScreen.withId(exercise.id))
            }) {
                Text(exercise.movementId.toString())
            }
        }
    }
}