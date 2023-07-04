package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.presentation.session.SessionEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseSetRow(
    set: Set,
    onEvent: (SessionEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${set.order}.",
            modifier = Modifier.weight(1f)
        )

        TextField(
            value = set.reps?.toString() ?: "",
            onValueChange = {
                onEvent(SessionEvent.UpdateSet(
                    set = set,
                    reps = it.toIntOrNull(),
                    weight = set.weight,
                    rpe = set.rpe
                ))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.weight(1f)
        )

        TextField(
            value = set.weight?.toString() ?: "",
            onValueChange = {
                onEvent(SessionEvent.UpdateSet(
                    set = set,
                    reps = set.reps,
                    weight = it.toFloatOrNull(),
                    rpe = set.rpe
                ))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.weight(1f)
        )

        TextField(
            value = set.rpe?.toString() ?: "",
            onValueChange = {
                onEvent(SessionEvent.UpdateSet(
                    set = set,
                    reps = set.reps,
                    weight = set.weight,
                    rpe = it.toFloatOrNull()
                ))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.weight(1f)
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