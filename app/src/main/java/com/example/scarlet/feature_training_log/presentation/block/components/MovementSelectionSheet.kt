package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.block.BlockEvent
import com.example.scarlet.feature_training_log.presentation.block.BlockUiState
import com.example.scarlet.feature_training_log.presentation.core.components.SecondaryActionButton

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MovementSelectionSheet(
    sheetState: BlockUiState.MovementSelectionSheetState,
    onEvent: (BlockEvent) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
            onEvent(BlockEvent.HideMovementSelectionSheet)
        },
        sheetState = rememberModalBottomSheetState(
             skipPartiallyExpanded = true
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.select_movement),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(modifier = Modifier.width(96.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (sheetState.movements.isEmpty()) {
                        item {
                            Text(
                                text = stringResource(R.string.no_movements_found),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            if (sheetState.movementNameFilter.isBlank()) {
                                Text(
                                    text = stringResource(R.string.enter_movement_name),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    sheetState.newMovementName?.let { addMovementName ->
                        item {
                            SecondaryActionButton(
                                onClick = {
                                    onEvent(BlockEvent.AddMovement)
                                },
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(
                                        R.string.add_new_movement_btn_msg,
                                        addMovementName
                                    )
                                )
                                Text(
                                    text = stringResource(
                                        R.string.add_new_movement_btn_msg,
                                        addMovementName
                                    ),
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                    items(sheetState.movements) { movement ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .combinedClickable(
                                    onClick = { onEvent(BlockEvent.SelectMovement(movement)) },
                                    onLongClick = {
                                        // do not allow superset selection when
                                        // editing an exercise's movement
                                        if (sheetState.exercise == null &&
                                            !sheetState.isInSupersetSelectionMode
                                        ) {
                                            onEvent(BlockEvent.EnableSupersetSelectionMode(movement))
                                        }
                                    }
                                )
                                .border(1.dp, MaterialTheme.colorScheme.onSurfaceVariant)
                                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AnimatedVisibility(visible = sheetState.isInSupersetSelectionMode) {
                                NumberedCheckBox(
                                    number = sheetState.supersetMovements.indexOf(movement) + 1,
                                    checked = movement in sheetState.supersetMovements,
                                    onCheckedChange = { onEvent(BlockEvent.SelectMovement(movement)) }
                                )
                            }
                            Text(
                                text = movement.name,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = {
                                onEvent(BlockEvent.ShowEditMovementSheet(movement))
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = stringResource(R.string.edit_movement)
                                )
                            }
                        }
                    }
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = sheetState.isInSupersetSelectionMode,
                    modifier = Modifier.align(Alignment.BottomCenter),
                    enter = slideInVertically { it }
                    // TODO change the exit transition
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(onClick = {
                            onEvent(BlockEvent.DisableSupersetSelectionMode)
                        }) {
                            Text(text = stringResource(R.string.cancel))
                        }
                        Button(onClick = {
                            onEvent(BlockEvent.AddSuperset)
                        }) {
                            Text(text = stringResource(R.string.add_superset))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                val focusRequester = remember { FocusRequester() }
                SideEffect { focusRequester.requestFocus() }
                OutlinedTextField(
                    value = sheetState.movementNameFilter,
                    onValueChange = {
                        onEvent(BlockEvent.FilterMovementsByName(it))
                    },
                    placeholder = {
                        Text(stringResource(R.string.enter_movement))
                    },
                    singleLine = true,
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .padding(start = 8.dp)
                        .weight(1f)
                )
                IconButton(onClick = {
                    onEvent(BlockEvent.FilterMovementsByName(""))
                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.clear)
                    )
                }
            }
        }
    }
}