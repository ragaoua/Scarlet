package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scarlet.R

@Composable
fun ExerciseDetailHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(modifier = Modifier.weight(0.5f)) {
            Text(
                text = stringResource(R.string.set),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Center)

            )
        }
        Box(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.reps),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.weight),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            Text(
                text = "RPE/RIR", /* TODO make clickable to choose one of the 2 */
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewExerciseDetailHeader() {
    ExerciseDetailHeader(
        modifier = Modifier.fillMaxWidth()
    )
}