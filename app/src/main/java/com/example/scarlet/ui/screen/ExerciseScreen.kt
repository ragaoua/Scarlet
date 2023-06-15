package com.example.scarlet.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scarlet.R
import com.example.scarlet.db.model.Exercise
import com.example.scarlet.db.model.Set
import com.example.scarlet.ui.theme.ScarletTheme
import com.example.scarlet.viewmodel.TrainingLogViewModel

@Composable
fun ExerciseScreen(
    exerciseId: Int,
    navController: NavController,
    trainingLogViewModel: TrainingLogViewModel
) {
    val exercise by trainingLogViewModel.getExerciseById(exerciseId).collectAsState(initial = null)
    val exerciseSets by trainingLogViewModel.getExerciseSetsById(exerciseId).collectAsState(initial = emptyList())

//    val movement by exercise?.let {
//        trainingLogViewModel.getMovementById(it.movementId).collectAsState(initial = null) }
    DisplayExerciseScreen(exercise, exerciseSets)
}

@Composable
fun DisplayExerciseScreen(
    exercise: Exercise?,
    sets: List<Set>
) {
    ScarletTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                exercise?.let {
                    ExerciseHeader(
                        movement = it.movementId.toString()
                    )
                    SetsSection(
                        sets = sets
                    )
                    //AddSetButton()
                }
            }
        }
    }
}

@Composable
fun ExerciseHeader(
    movement: String
) {
    Text(
        text = movement,
        fontSize = 20.sp
    )
}

@Composable
fun SetsSection(
    sets: List<Set>
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(50.dp)
    ) {
        sets.forEach { set ->
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
            horizontal = 32.dp/*,
            vertical = 5.dp*/)
    ) {
        Text(text = "${set.order.toString()}.")
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
        Spacer(modifier = Modifier.width(10.dp))
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
        Spacer(modifier = Modifier.width(10.dp))
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
    Button(onClick = {  }) {
        Text("+")
    }
}

@Composable
@Preview
fun Preview() {
    val exercise = Exercise(1, 1, 1, 1)
    val sets = listOf(
        Set(1, 1, 1, 10, 100f, 6f),
        Set(2, 1, 2, 20, 200f, 10f)
    )
    DisplayExerciseScreen(
        exercise = exercise,
        sets = sets)
}