package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.scarlet.R

@Composable
fun ExerciseDetailHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = stringResource(R.string.set),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = stringResource(R.string.reps),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = stringResource(R.string.weight),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "RPE/RIR", /* TODO make clickable to choose one of the 2 */
            style = MaterialTheme.typography.titleMedium
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewExerciseDetailHeader() {
    ExerciseDetailHeader(
        modifier = Modifier.fillMaxWidth()
    )
}