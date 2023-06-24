package com.example.scarlet.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scarlet.db.model.Exercise
import com.example.scarlet.db.model.Session
import com.example.scarlet.ui.events.SessionEvent
import com.example.scarlet.ui.navigation.SessionScreenNavArgs
import com.example.scarlet.ui.screen.destinations.ExerciseScreenDestination
import com.example.scarlet.ui.states.SessionUiState
import com.example.scarlet.ui.theme.ScarletTheme
import com.example.scarlet.viewmodel.SessionViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Destination(
    navArgsDelegate = SessionScreenNavArgs::class
)
@Composable
fun SessionScreen(
    navigator: DestinationsNavigator
) {
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val state by sessionViewModel.state.collectAsState()

    Screen(
        navigator = navigator,
        state = state,
        onEvent = sessionViewModel::onEvent
    )
}

@Composable
fun Screen(
    navigator: DestinationsNavigator,
    state: SessionUiState,
    onEvent: (SessionEvent) -> Unit
) {
    ScarletTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SessionHeader(
                sessionDate = state.session.date
            )
            ExercisesSection(
                exercises = state.exercises,
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
        modifier = Modifier
            .fillMaxWidth()
        ,
        text = sessionDate,
        fontSize = 20.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
fun ExercisesSection(
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
fun PreviewEmptySession() {
    Screen(
        navigator = EmptyDestinationsNavigator,
        state = SessionUiState(
            session = Session(date = "24-06-2023")
        ),
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewScreen() {
    Screen(
        navigator = EmptyDestinationsNavigator,
        state = SessionUiState(
            session = Session(date = "24-06-2023"),
            exercises = listOf(
                Exercise(movementId = 1),
                Exercise(movementId = 2),
                Exercise(movementId = 3)
            )
        ),
        onEvent = {}
    )
}