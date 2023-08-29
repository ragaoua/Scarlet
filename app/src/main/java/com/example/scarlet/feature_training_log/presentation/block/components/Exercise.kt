package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.presentation.block.BlockEvent
import com.example.scarlet.feature_training_log.presentation.core.components.SecondaryActionButton

@Composable
fun Exercise(
    exercise: ExerciseWithMovementAndSets,
    isInEditMode: Boolean,
    onEvent: (BlockEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface)
            .border(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                shape = MaterialTheme.shapes.medium
            )
    ) {
        /*************************************************************************
         * EXERCISE HEADER
         *************************************************************************/
        var isExerciseDetailExpanded by remember(isInEditMode) { mutableStateOf(!isInEditMode) }

        Row(
            modifier =
            if (!isInEditMode) {
                Modifier.clickable { isExerciseDetailExpanded = !isExerciseDetailExpanded }
            } else {
                Modifier
            }
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = exercise.movement.name,
                style = MaterialTheme.typography.titleLarge
            )
            if(isInEditMode) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.clickable {
                            // TODO
//                            onEvent(SessionEvent.ShowMovementSelectionSheet(exercise.toExercise()))
                        },
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.select_movement)
                    )
                    Icon(
                        modifier = Modifier.clickable {
                            // TODO
//                            onEvent(SessionEvent.DeleteExercise(exercise.toExercise()))
                        },
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete)
                    )
                }
            } else {
                if (isExerciseDetailExpanded) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = stringResource(R.string.collapse_details)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.expand_details)
                    )
                }
            }
        }

        /*************************************************************************
         * EXERCISE DETAIL
         *************************************************************************/
        AnimatedVisibility(visible = isExerciseDetailExpanded) {
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (exercise.sets.isNotEmpty()) {
                    ExerciseDetailHeader(
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier) // Doubles the space between header and sets
                    exercise.sets.forEach { set ->
                        // TODO
                        Text(text = set.toString())
//                        ExerciseSetRow(
//                            set = set,
//                            isFirstSet = set == exercise.sets.first(),
//                            isLastSet = set == exercise.sets.last(),
//                            onEvent = onEvent
//                        )
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
                        // TODO
//                        onEvent(SessionEvent.AddSet(exercise.toExercise()))
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