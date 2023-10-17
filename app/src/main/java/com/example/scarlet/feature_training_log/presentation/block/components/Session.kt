package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.UnfoldLess
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.core.util.isScrollingDown
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.domain.model.SessionWithExercises
import com.example.scarlet.feature_training_log.presentation.block.BlockEvent
import com.example.scarlet.feature_training_log.presentation.core.DateUtils
import kotlinx.coroutines.delay

@Composable
fun Session(
    modifier: Modifier = Modifier,
    session: SessionWithExercises<ExerciseWithMovementAndSets>,
    isExerciseDetailExpandedById: Map<Long, Boolean>,
    expandedDropdownMenuExerciseId: Long?,
    isInSessionEditMode: Boolean,
    onEvent: (BlockEvent) -> Unit
) {
    val scrollState = rememberScrollState()
    val isScrollingDown = scrollState.isScrollingDown()
    LaunchedEffect(isScrollingDown) {
        delay(100)
        if (isScrollingDown) {
            onEvent(BlockEvent.HideFloatingActionButtons)
        } else {
            onEvent(BlockEvent.ShowFloatingActionButtons)
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
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
                                onEvent(BlockEvent.ToggleSessionExercisesDetails(session.id))
                            }) {
                                AnimatedContent(
                                    targetState = session.exercises.none {
                                        isExerciseDetailExpandedById[it.id] == true
                                    },
                                    label = "Collapse/expand sessions exercises icon",
                                    transitionSpec = {
                                        scaleIn() togetherWith scaleOut()
                                    }
                                ) { allExercisesCollapsed ->
                                    if (allExercisesCollapsed) {
                                        Icon(
                                            imageVector = Icons.Default.UnfoldMore,
                                            contentDescription = stringResource(R.string.expand_session_exercises_details)
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.UnfoldLess,
                                            contentDescription = stringResource(R.string.collapse_session_exercises_details)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    AnimatedVisibility (visible = isInSessionEditMode) {
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
                            expandedDropdownMenuExerciseId = expandedDropdownMenuExerciseId,
                            isInSessionEditMode = isInSessionEditMode,
                            onEvent = onEvent
                        )
                    } else {
                        val exercise = exercises.first()
                        Exercise(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            session = session,
                            exercise = exercise,
                            isExerciseDetailExpanded = isExerciseDetailExpandedById[exercise.id] == true,
                            isDropdownMenuExpanded = expandedDropdownMenuExerciseId == exercise.id,
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

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}