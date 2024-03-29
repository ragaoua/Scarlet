package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.presentation.block.BlockEvent

@Composable
fun Superset(
    exercises: List<ExerciseWithMovementAndSets>,
    lastExerciseOrder: Int,
    isExerciseDetailExpandedById: Map<Long, Boolean>,
    expandedDropdownMenuExerciseId: Long?,
    isInSessionEditMode: Boolean,
    onEvent: (BlockEvent) -> Unit
) {
    LabeledBorderBox(
        label = {
            Text(
                text = stringResource(R.string.superset),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            exercises.forEach { exercise ->
                Exercise(
                    exercise = exercise,
                    lastExerciseOrder = lastExerciseOrder,
                    lastSupersetOrder = exercises.last().supersetOrder,
                    isExerciseDetailExpanded = isExerciseDetailExpandedById[exercise.id] == true,
                    isDropdownMenuExpanded = expandedDropdownMenuExerciseId == exercise.id,
                    isInSessionEditMode = isInSessionEditMode,
                    onEvent = onEvent
                )
            }
        }
    }
}