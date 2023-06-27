package com.example.scarlet.feature_training_log.presentation.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.ui.theme.ScarletTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Destination(
    navArgsDelegate = ExerciseScreenNavArgs::class
)
@Composable
fun ExerciseScreen(
    navigator: DestinationsNavigator
) {
    val exerciseViewModel: ExerciseViewModel = hiltViewModel()
    val state by exerciseViewModel.state.collectAsState()

    Screen(
        navigator = navigator,
        state = state,
        onEvent = exerciseViewModel::onEvent
    )
}

@Composable
fun Screen(
    navigator: DestinationsNavigator,
    state: ExerciseUiState,
    onEvent: (ExerciseEvent) -> Unit
) {
    ScarletTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ExerciseHeader(
                    state = state
                )
                SetsSection(
                    state = state
                )
                //AddSetButton()
            }
        }
    }
}

@Composable
fun ExerciseHeader(
    state: ExerciseUiState
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = state.exercise.movementId.toString(),
        textAlign = TextAlign.Center,
        fontSize = 20.sp
    )
}

@Composable
fun SetsSection(
    state: ExerciseUiState
) {
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(50.dp)
    ) {
        state.sets.forEach { set ->
            DisplaySet(set)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplaySet(
    set: Set
) {
    var repsState by remember { mutableStateOf(set.reps.toString()) }
    var weightState by remember { mutableStateOf(set.weight.toString()) }
    var rpeState by remember { mutableStateOf(set.rpe?.toString()) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(
            horizontal = 32.dp)
    ) {
        Text(text = "${set.order}.")
        Spacer(modifier = Modifier.width(10.dp))
        TextField(
            value = repsState,
            label = {
                Text(stringResource(R.string.reps))
            },
            onValueChange = {
                repsState = it
            },
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
        TextField(
            value = weightState,
            label = {
                Text(stringResource(R.string.weight))
            },
            onValueChange = {
                weightState = it
            },
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
        TextField(
            value = rpeState ?: "" ,
            label = {
                Text(stringResource(R.string.rpe))
            },
            onValueChange = {
                rpeState = it
            },
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun AddSetButton() {
    Button(onClick = {
        /* TODO */
    }) {
        Text("+")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEmptyExercise() {
    Screen(
        navigator = EmptyDestinationsNavigator,
        state = ExerciseUiState(
            exercise = Exercise()
        ),
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewExerciseScreen() {
    Screen(
        navigator = EmptyDestinationsNavigator,
        state = ExerciseUiState(
            exercise = Exercise(),
            sets = listOf(
                Set(1, 1, 1, 10, 100f, 6f),
                Set(2, 1, 2, 20, 200f, 10f)
            )),
        onEvent = {}
    )
}



