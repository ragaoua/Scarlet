package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Expand
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercises
import com.example.scarlet.feature_training_log.presentation.block.BlockEvent
import com.example.scarlet.feature_training_log.presentation.core.DateUtils

@Composable
fun Session(
    modifier: Modifier = Modifier,
    session: SessionWithExercises<ExerciseWithMovementAndSets>,
    isExerciseDetailExpandedById: Map<Long, Boolean>,
    isInSessionEditMode: Boolean,
    onEvent: (BlockEvent) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = DateUtils.formatDate(session.date),
                    style = MaterialTheme.typography.titleLarge
                )
                Row (verticalAlignment = Alignment.CenterVertically) {
                    AnimatedVisibility (visible = !isInSessionEditMode) {
                        IconButton(onClick = {
                            onEvent(BlockEvent.ToggleSessionExercisesDetails(session.id))
                        }) {
                            Icon(
                                imageVector = Icons.Default.Expand, // TODO change icon when expanded
                                contentDescription = stringResource(R.string.expand_session_exercises_details) // TODO change description when expanded
                            )
                        }
                    }
                    AnimatedVisibility (visible = isInSessionEditMode) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = {
                                onEvent(BlockEvent.ShowSessionDatePickerDialog(session.toSession()))
                            }) {
                                Icon(
                                    imageVector = Icons.Default.EditCalendar,
                                    contentDescription = stringResource(R.string.edit_session_date)
                                )
                            }
                            IconButton(onClick = {
                                onEvent(BlockEvent.DeleteSession(session.toSession()))
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete)
                                )
                            }
                        }
                    }
                }
            }
            HorizontalDivider()

            if (session.exercises.isNotEmpty()) {
                session.exercises.groupBy { it.order }.values.forEach { exercises ->
                    // If there is more than one exercise with the same order,
                    // display them as a superset
                    if (exercises.size > 1) {
                        Superset(
                            session = session,
                            exercises = exercises,
                            isExerciseDetailExpandedById = isExerciseDetailExpandedById,
                            isInSessionEditMode = isInSessionEditMode,
                            onEvent = onEvent
                        )
                    } else {
                        Exercise(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            session = session,
                            exercise = exercises.first(),
                            isExerciseDetailExpandedById = isExerciseDetailExpandedById,
                            isInSessionEditMode = isInSessionEditMode,
                            onEvent = onEvent
                        )
                    }
                }
            } else {
                Text(
                    text = stringResource(R.string.empty_session),
                    style = MaterialTheme.typography.bodyMedium
                    // TODO color = grey
                )
            }
            if (!isInSessionEditMode) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.small,
                    onClick = {
                        onEvent(
                            BlockEvent.ShowMovementSelectionSheet(
                                session = session
                            )
                        )
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
}