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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
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
    val reps = remember(key1 = set.reps) { mutableStateOf(
        TextFieldValue(set.reps?.toString() ?: "")
    )}
    val weight = remember(key1 = set.weight) { mutableStateOf(
        TextFieldValue(set.weight?.toString() ?: "")
    )}
    val rpe = remember(key1 = set.rpe) { mutableStateOf(
        TextFieldValue(set.rpe?.toString() ?: "")
    )}

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
                if (reps.value.text == it.text) {
                    reps.value = it.copy(selection = reps.value.selection)
                } else {
                    reps.value = it
                }
            },
            onFocusChanged = {
                if(it.isFocused) {
                    reps.value = reps.value.copy(
                        selection = TextRange(0, reps.value.text.length)
                    )
                } else {
                    reps.value = reps.value.copy(selection = TextRange(0, 0))
                    if (reps.value.text != (set.reps?.toString() ?: "")) {
                        onEvent(SessionEvent.UpdateSet(
                            set.copy(reps = reps.value.text.toIntOrNull())
                        ))
                    }
                }
            },
            imeAction = ImeAction.Next
        )

        SetTextField(
            modifier = Modifier.weight(1f),
            value = weight.value,
            onValueChange = {
                if (weight.value.text == it.text) {
                    weight.value = it.copy(selection = weight.value.selection)
                } else {
                    weight.value = it
                }
            },
            onFocusChanged = {
                if(it.isFocused) {
                    weight.value = weight.value.copy(
                        selection = TextRange(0, weight.value.text.length)
                    )
                } else {
                    weight.value = weight.value.copy(selection = TextRange(0, 0))
                    if (weight.value.text != (set.weight?.toString() ?: "")) {
                        onEvent(SessionEvent.UpdateSet(
                            set.copy(weight = weight.value.text.toFloatOrNull())
                        ))
                    }
                }
            },
            imeAction = ImeAction.Next
        )

        SetTextField(
            modifier = Modifier.weight(1f),
            value = rpe.value,
            onValueChange = {
                if (rpe.value.text == it.text) {
                    rpe.value = it.copy(selection = rpe.value.selection)
                } else {
                    rpe.value = it
                }
            },
            onFocusChanged = {
                if(it.isFocused) {
                    rpe.value = rpe.value.copy(
                        selection = TextRange(0, rpe.value.text.length)
                    )
                } else {
                    rpe.value = rpe.value.copy(selection = TextRange(0, 0))
                    if (rpe.value.text != (set.rpe?.toString() ?: "")) {
                        onEvent(SessionEvent.UpdateSet(
                            set.copy(rpe = rpe.value.text.toFloatOrNull())
                        ))
                    }
                }
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