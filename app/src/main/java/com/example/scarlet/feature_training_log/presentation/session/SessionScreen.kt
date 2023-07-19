package com.example.scarlet.feature_training_log.presentation.session

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Session
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.presentation.session.components.ExercisesList
import com.example.scarlet.feature_training_log.presentation.session.components.MovementSelectionSheet
import com.example.scarlet.feature_training_log.presentation.session.components.SessionHeader
import com.example.scarlet.ui.theme.ScarletTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.Date

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
        state = state,
        onEvent = sessionViewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(
    state: SessionUiState,
    onEvent: (SessionEvent) -> Unit
) {
    ScarletTheme {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        onEvent(SessionEvent.ToggleEditMode) // TODO save changes
                    },
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    if (state.isInEditMode) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.save_session)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.edit_session)
                        )
                    }
                }
                /* TODO change the color */
            }
        ) { contentPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                if (state.isMovementSelectionSheetOpen) {
                    MovementSelectionSheet(
                        state = state,
                        onEvent = onEvent
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(48.dp))
                    SessionHeader(
                        state = state,
                        onEvent = onEvent
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    ExercisesList(
                        exercises = state.exercises,
                        onEvent = onEvent
                    )
                }
            }
        }
        if(state.isMovementSelectionSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = {
                    onEvent(SessionEvent.CollapseMovementSelectionSheet)
                }
            ) {
                MovementSelectionSheet(
                    state = state,
                    onEvent = onEvent
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewEmptySession() {
    Screen(
        state = SessionUiState(
            session = Session(
                date = Date(System.currentTimeMillis())
            )
        ),
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSessionScreen() {
    Screen(
        state = SessionUiState(
            session = Session(
                date = Date(System.currentTimeMillis())
            ),
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