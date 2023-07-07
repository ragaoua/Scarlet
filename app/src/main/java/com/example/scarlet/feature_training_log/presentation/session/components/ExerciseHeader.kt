package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.Movement

@Composable
fun ExerciseHeader(
    exercise: ExerciseWithMovementAndSets
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface))
            .clickable {
                /* TODO expand/collapse detail */
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = exercise.movement.name,
                style = MaterialTheme.typography.titleLarge
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Expand"
            ) /* TODO KeyboardArrowDown/Up depending on expand/collapse state */
        }

    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHeader() {
    ExerciseHeader(
        exercise = ExerciseWithMovementAndSets(
            exercise = Exercise(),
            movement = Movement(name = "Low-bar Squat"),
            sets = emptyList()
        )
    )
}