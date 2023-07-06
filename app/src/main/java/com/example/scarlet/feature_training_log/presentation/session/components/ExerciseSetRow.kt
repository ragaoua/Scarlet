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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.presentation.session.SessionEvent

@Composable
fun ExerciseSetRow(
    set: Set,
    onEvent: (SessionEvent) -> Unit
) {
    val reps = remember(key1 = set.reps) { mutableStateOf(set.reps?.toString() ?: "") }
    val weight = remember(key1 = set.weight) { mutableStateOf(set.weight?.toString() ?: "") }
    val rpe = remember(key1 = set.rpe) { mutableStateOf(set.rpe?.toString() ?: "") }

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
            value = reps.value,
            onValueChange = {
                reps.value = it
            },
            onFocusChanged = {
                if (!it.isFocused && reps.value != (set.reps?.toString() ?: "")) {
                    onEvent(SessionEvent.UpdateSet(
                        set.copy(reps = reps.value.toIntOrNull())
                    ))
                }
            }
        )

        SetTextField(
            modifier = Modifier.weight(1f),
            value = weight.value,
            onValueChange = {
                weight.value = it
            },
            onFocusChanged = {
                if (!it.isFocused && weight.value != (set.weight?.toString() ?: "")) {
                    onEvent(SessionEvent.UpdateSet(
                        set.copy(weight = weight.value.toFloatOrNull())
                    ))
                }
            }
        )

        SetTextField(
            modifier = Modifier.weight(1f),
            value = rpe.value,
            onValueChange = {
                rpe.value = it
            },
            onFocusChanged = {
                if (!it.isFocused && rpe.value != (set.rpe?.toString() ?: "")) {
                    onEvent(SessionEvent.UpdateSet(
                        set.copy(rpe = rpe.value.toFloatOrNull())
                    ))
                }
            }
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


@Preview(showBackground = true)
@Composable
fun Preview_set() {
    ExerciseSetRow(
        set = Set(reps = 10, weight = 100f, rpe = 8f),
        onEvent = {}
    )
}