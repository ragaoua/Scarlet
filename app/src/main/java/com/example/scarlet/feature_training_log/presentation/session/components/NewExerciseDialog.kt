package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.session.SessionEvent
import com.example.scarlet.feature_training_log.presentation.session.SessionUiState

@Composable
fun NewExerciseDialog(
    state: SessionUiState,
    onEvent: (SessionEvent) -> Unit
) {
    var selectedMovementId by remember { mutableStateOf(0) }
    AlertDialog(
        onDismissRequest = {
            onEvent(SessionEvent.HideNewExerciseDialog)
        },
        title = {
            Text(stringResource(R.string.new_exercise))
        },
        text  = {
            DropdownMenu(
                expanded = state.isAddingExercise,
                onDismissRequest = {
                    onEvent(SessionEvent.HideNewExerciseDialog)
                }
            ) {
                state.movements.forEach { movement ->
                    DropdownMenuItem(
                        text = { Text(movement.name) },
                        onClick = {
                            selectedMovementId = movement.id
                            onEvent(SessionEvent.NewExercise(selectedMovementId))
                        }
                    )
                }
            }
        },
        confirmButton = {
//            Button(onClick = {
//                onEvent(SessionEvent.NewExercise(selectedMovementId))
//            }) {
//                Text(stringResource(R.string.add_exercise))
//            }
        },
        dismissButton = {
//            Button(onClick = {
//                onEvent(SessionEvent.HideNewExerciseDialog)
//            }) {
//                Text(stringResource(R.string.cancel))
//            }
        }
    )
}

@Preview
@Composable
fun DialogPreview() {
    NewExerciseDialog(
        state = SessionUiState(),
        onEvent = {}
    )
}
//
//@Preview
//@Composable
//fun ErrorMessagePreview() {
//    NewBlockDialog(
//        state = TrainingLogUiState(
//            isShowingBlockNameEmptyMsg = true
//        ),
//        onEvent = {}
//    )
//}