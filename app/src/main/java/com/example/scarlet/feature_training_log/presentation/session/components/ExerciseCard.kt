package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.presentation.core.components.SecondaryActionButton
import com.example.scarlet.feature_training_log.presentation.session.SessionEvent

@Composable
fun ExerciseCard(
    exercise: ExerciseWithMovementAndSets,
    isInEditMode: Boolean,
    onEvent: (SessionEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .border(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                shape = MaterialTheme.shapes.medium
            )
    ) {
        /*************************************************************************
         * EXERCISE HEADER
         *************************************************************************/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    /* TODO expand/collapse detail */
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = exercise.movement.name,
                style = MaterialTheme.typography.titleLarge
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Expand" /* localize */
            ) /* TODO KeyboardArrowDown/Up depending on expand/collapse state */
        }

        /*************************************************************************
         * EXERCISE DETAIL
         *************************************************************************/
        if (!isInEditMode) {
            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (exercise.sets.isNotEmpty()) {
                    ExerciseDetailHeader(
                        modifier = Modifier.fillMaxWidth()
                    )
                    exercise.sets.forEach { set ->
                        ExerciseSetRow(
                            set = set,
                            onEvent = onEvent
                        )
                    }
                } else {
                    Text(text = stringResource(R.string.no_sets_msg))
                    /* TODO change color (grey) and style */
                }

                /*************************************************************************
                 * "ADD SET" BUTTON
                 *************************************************************************/
                SecondaryActionButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    onClick = {
                        onEvent(SessionEvent.AddSet(exercise.exercise))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_set)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewExerciseDetail_noSets() {
    ExerciseCard(
        exercise = ExerciseWithMovementAndSets(
            exercise = Exercise(),
            movement = Movement(),
            sets = emptyList()
        ),
        isInEditMode = false,
        onEvent = {}
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewExerciseDetail_withSets() {
    ExerciseCard(
        exercise = ExerciseWithMovementAndSets(
            exercise = Exercise(),
            movement = Movement(),
            sets = listOf(
                Set(order = 1, reps = 10, weight = 100f, rpe = 8f),
                Set(order = 2, reps = 10, weight = 100f, rpe = 8.5f),
                Set(order = 3, reps = 10, weight = 95f, rpe = 8f)
            )
        ),
        isInEditMode = false,
        onEvent = {}
    )
}