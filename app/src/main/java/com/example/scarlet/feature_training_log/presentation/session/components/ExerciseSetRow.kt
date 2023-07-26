package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.presentation.session.SessionEvent

@Composable
fun ExerciseSetRow(
    set: Set,
    isLastSet: Boolean,
    onEvent: (SessionEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(modifier = Modifier.weight(0.5f)) {
            Text(
                text = "${set.order}.",
                modifier = Modifier.align(Alignment.Center)
            )
        }

        SetTextField(
            modifier = Modifier.weight(1f),
            originalValue = set.reps?.toString() ?: "",
            updateSet = { text ->
                onEvent(SessionEvent.UpdateSet(
                    set.copy(reps = text.toIntOrNull())
                ))
            },
            imeAction = ImeAction.Next
        )

        SetTextField(
            modifier = Modifier.weight(1f),
            originalValue = set.weight?.toString() ?: "",
            updateSet = { text ->
                onEvent(SessionEvent.UpdateSet(
                    set.copy(weight = text.toFloatOrNull())
                ))
            },
            imeAction = ImeAction.Next
        )

        SetTextField(
            modifier = Modifier.weight(1f),
            originalValue = set.rpe?.toString() ?: "",
            updateSet = { text ->
                onEvent(SessionEvent.UpdateSet(
                    set.copy(rpe = text.toFloatOrNull())
                ))
            },
            imeAction = if (isLastSet) ImeAction.Done else ImeAction.Next
        )

        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(R.string.delete),
            modifier = Modifier
                .weight(0.5f)
                .clickable {
                    onEvent(SessionEvent.DeleteSet(set))
                }
        )
    }
}