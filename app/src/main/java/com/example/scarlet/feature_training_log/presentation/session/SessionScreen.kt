package com.example.scarlet.feature_training_log.presentation.session

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.presentation.session.components.ExercisesList
import com.example.scarlet.feature_training_log.presentation.session.components.SessionHeader
import com.example.scarlet.ui.theme.ScarletTheme
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
                session = state.session
            )
            ExercisesList(
                exercises = state.exercises,
                onEvent = onEvent
            )
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
fun PreviewSessionScreen() {
    Screen(
        navigator = EmptyDestinationsNavigator,
        state = SessionUiState(
            session = Session(date = "24-06-2023"),
            exercises = listOf(
                ExerciseWithMovementAndSets(Exercise(), Movement(name="Squat"), listOf(
                    Set(reps = 3, weight = 100f),
                    Set(reps = 5, weight = 95f, rpe = 8.5f),
                    Set(reps = 5, weight = 95f, rpe = 8.5f),
                    Set(reps = 5, weight = 95f, rpe = 8.5f)
                )),
                ExerciseWithMovementAndSets(Exercise(), Movement(name="Bench"), emptyList()),
                ExerciseWithMovementAndSets(Exercise(), Movement(name="Deadlift"), emptyList())
            )
        ),
        onEvent = {}
    )
}