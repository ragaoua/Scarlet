package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.scarlet.feature_training_log.domain.model.Set

@Composable
fun ExerciseSetRow(
    set: Set
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("${set.order}")
        Text("${set.reps}")
        Text("${set.weight}")
        Text("${set.rpe}")
    }
}


@Preview(showBackground = true)
@Composable
fun Preview_set() {
    ExerciseSetRow(
        set = Set(reps = 10, weight = 100f, rpe = 8f)
    )
}