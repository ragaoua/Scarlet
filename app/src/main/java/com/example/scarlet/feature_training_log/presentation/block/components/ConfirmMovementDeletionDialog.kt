package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.scarlet.R

@Composable
fun ConfirmMovementDeletionDialog(
    movement: String,
    nbExercises: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource( R.string.confirm_movement_deletion),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = if (nbExercises == 0) {
                    stringResource(R.string.no_exercise_associated_with_movement)
                } else pluralStringResource(
                    R.plurals.confirm_movement_deletion_message,
                    nbExercises,
                    movement,
                    nbExercises
                ),
                textAlign = TextAlign.Center
            )
        }
    )
}