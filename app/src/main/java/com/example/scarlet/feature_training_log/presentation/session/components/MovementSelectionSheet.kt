package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.runtime.Composable
import com.example.scarlet.feature_training_log.presentation.session.SessionEvent
import com.example.scarlet.feature_training_log.presentation.session.SessionUiState

@Composable
fun MovementSelectionSheet(
    state: SessionUiState,
    onEvent: (SessionEvent) -> Unit
) {

//    var selectedMovementId by remember { mutableStateOf(0) }
//    AlertDialog(
//        onDismissRequest = {
//            onEvent(SessionEvent.CollapseMovementSelectionSheet)
//        },
//        title = {
//            Text(stringResource(R.string.new_exercise))
//        },
//        text  = {
//            DropdownMenu(
//                expanded = state.isMovementSelectionSheetOpen,
//                onDismissRequest = {
//                    onEvent(SessionEvent.CollapseMovementSelectionSheet)
//                }
//            ) {
//                state.movements.forEach { movement ->
//                    DropdownMenuItem(
//                        text = { Text(movement.name) },
//                        onClick = {
//                            selectedMovementId = movement.id
//                            onEvent(SessionEvent.NewExercise(selectedMovementId))
//                        }
//                    )
//                }
//            }
//        },
//        confirmButton = {
//            Button(onClick = {
//                onEvent(SessionEvent.NewExercise(selectedMovementId))
//            }) {
//                Text(stringResource(R.string.add_exercise))
//            }
//        },
//        dismissButton = {
//            Button(onClick = {
//                onEvent(SessionEvent.HideNewExerciseDialog)
//            }) {
//                Text(stringResource(R.string.cancel))
//            }
//        }
//    )
}