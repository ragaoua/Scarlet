package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.scarlet.feature_training_log.domain.model.Set

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseSetRow(
    set: Set
) {
    var repsState by remember { mutableStateOf(set.reps.toString()) }
    var weightState by remember { mutableStateOf(set.weight.toString()) }
    var rpeState by remember { mutableStateOf(set.rpe?.toString()) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${set.order}.",
            modifier = Modifier.weight(1f)
        )

        TextField(
            value = repsState,
            onValueChange = {
                repsState = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.weight(1f)
        )

        TextField(
            value = weightState,
            onValueChange = {
                weightState = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.weight(1f)
        )

        TextField(
            value = rpeState ?: "",
            onValueChange = {
                rpeState = it
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
        set = Set(reps = 10, weight = 100f, rpe = 8f)
    )
}