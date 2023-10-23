package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.core.util.conditional
import com.example.scarlet.feature_training_log.domain.model.ExerciseWithMovementAndSets
import com.example.scarlet.feature_training_log.presentation.block.BlockEvent
import com.example.scarlet.feature_training_log.presentation.core.components.SecondaryActionButton

@Composable
fun Exercise(
    modifier: Modifier = Modifier,
    exercise: ExerciseWithMovementAndSets,
    lastExerciseOrder: Int,
    lastSupersetOrder: Int,
    isExerciseDetailExpanded: Boolean,
    isDropdownMenuExpanded: Boolean,
    isInSessionEditMode: Boolean,
    onEvent: (BlockEvent) -> Unit
) {
    Column(
        modifier = modifier
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
        Row(
            modifier = Modifier
                .conditional(!isInSessionEditMode) {
                    clickable { onEvent(BlockEvent.ToggleExerciseDetail(exercise.id)) }
                }
                .fillMaxWidth()
                .padding(start = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(!isInSessionEditMode) {
                AnimatedContent(
                    targetState = isExerciseDetailExpanded,
                    label = "Collapse/expand exercise icon",
                    transitionSpec = { scaleIn() togetherWith scaleOut() }
                ) { isExerciseDetailExpanded ->
                    if (isExerciseDetailExpanded) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = stringResource(R.string.collapse_details)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = stringResource(R.string.expand_details)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp),
                text = exercise.movement.name,
                style = MaterialTheme.typography.titleLarge
            )
            if(!isInSessionEditMode) {
                Box {
                    IconButton(onClick = {
                        onEvent(BlockEvent.ShowExerciseDropdownMenu(exercise.id))
                    }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(R.string.select_movement)
                        )
                    }
                    DropdownMenu(
                        expanded = isDropdownMenuExpanded,
                        onDismissRequest = { onEvent(BlockEvent.DismissExerciseDropdownMenu) }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row (verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = stringResource(R.string.delete)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(stringResource(R.string.delete))
                                }
                            },
                            onClick = { onEvent(BlockEvent.DeleteExercise(exercise)) }
                        )
                        DropdownMenuItem(
                            text = {
                                Row (verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = stringResource(R.string.edit_movement)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(stringResource(R.string.edit_movement))
                                }
                            },
                            onClick = {
                                onEvent(BlockEvent.ShowMovementSelectionSheet(
                                    sessionId = exercise.sessionId,
                                    exercise = exercise.toExercise()
                                ))
                            }
                        )
                    }
                }
            } else {
                IconButton(
                    onClick = { onEvent(BlockEvent.MoveExerciseDown(exercise.toExercise())) },
                    enabled = exercise.order < lastExerciseOrder || exercise.supersetOrder < lastSupersetOrder
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.move_down)
                    )
                }
                IconButton(
                    onClick = { onEvent(BlockEvent.MoveExerciseUp(exercise.toExercise())) },
                    enabled = exercise.order > 1 || exercise.supersetOrder > 1
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = stringResource(R.string.move_up)
                    )
                }
            }
        }

        /*************************************************************************
         * EXERCISE DETAIL
         *************************************************************************/
        AnimatedVisibility(
            visible = (isExerciseDetailExpanded) && !isInSessionEditMode
        ) {
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
                        modifier = Modifier.fillMaxWidth(),
                        ratingType = exercise.ratingType,
                        onRatingTypeChange = { ratingType ->
                            onEvent(BlockEvent.UpdateRatingType(
                                exercise = exercise.toExercise(),
                                ratingType = ratingType
                            ))
                        }
                    )
                    Spacer(modifier = Modifier) // Doubles the space between header and sets
                    exercise.sets.forEachIndexed { index, set ->
                        val previousSet = exercise.sets.getOrNull(index - 1)
                        ExerciseSetRow(
                            modifier = Modifier.fillMaxWidth(),
                            set = set,
                            isCopyRepsIconVisible = previousSet?.let {
                                it.reps != null && it.reps != set.reps
                            } ?: false,
                            isCopyLoadIconVisible = previousSet?.let {
                                // contrary to reps, here we want to display the icon even if the
                                // load is the same, because of the long click feature that
                                // displays the load calculation dialog
                                it.load != null
                            } ?: false,
                            isLastSet = set == exercise.sets.last(),
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
                        onEvent(BlockEvent.AddSet(exercise.toExercise()))
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