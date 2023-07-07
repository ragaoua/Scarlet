package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scarlet.feature_training_log.presentation.session.SessionEvent

@Composable
fun NewExerciseButton(
    modifier: Modifier = Modifier,
    onEvent: (SessionEvent) -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .border(BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface))
                .clickable {
                    onEvent(SessionEvent.ShowNewExerciseDialog)
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "New Exercise" /* TODO: create a string resource */
            )
        }
    }
}