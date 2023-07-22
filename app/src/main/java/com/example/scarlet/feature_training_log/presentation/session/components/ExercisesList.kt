package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.presentation.core.components.TitledLazyList
import com.example.scarlet.feature_training_log.presentation.session.SessionEvent
import com.example.scarlet.ui.theme.TitleLazyListPadding

@Composable
fun ExercisesList(
    exercises: List<ExerciseWithMovementAndSets>,
    onEvent: (SessionEvent) -> Unit
) {
    TitledLazyList(
        modifier = Modifier
            .fillMaxWidth()
            .padding(TitleLazyListPadding),
        title = stringResource(R.string.session_exercises_list_title)
    ) {
        if(exercises.isNotEmpty()) {
            items(exercises) { exercise ->
                ExerciseCard(
                    exercise = exercise,
                    onEvent = onEvent
                )
            }
        } else {
            item {
                Text(
                    text = stringResource(R.string.empty_session),
                    style = MaterialTheme.typography.bodyMedium
                    // TODO color = grey
                )
            }
        }
        item {
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                onClick = {
                    onEvent(SessionEvent.ToggleMovementSelectionSheet)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.new_exercise)
                )
            }
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