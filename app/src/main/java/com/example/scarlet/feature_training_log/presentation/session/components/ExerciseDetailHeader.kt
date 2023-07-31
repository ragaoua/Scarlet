package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.core.components.LabeledSwitch
import com.example.scarlet.feature_training_log.presentation.session.util.SetFieldRatio

@Composable
fun ExerciseDetailHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Box(modifier = Modifier.weight(SetFieldRatio.SET)) {
            Text(
                text = stringResource(R.string.set),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Box(modifier = Modifier.weight(SetFieldRatio.REPS)) {
            Text(
                text = stringResource(R.string.reps),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Box(modifier = Modifier.weight(SetFieldRatio.WEIGHT)) {
            Text(
                text = stringResource(R.string.weight),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Box(modifier = Modifier.weight(SetFieldRatio.RPE)) {
            LabeledSwitch(
                modifier = Modifier.align(Alignment.Center),
                lText = stringResource(R.string.rpe),
                rText = stringResource(R.string.rir),
                onValueChange = { /* TODO */ }
            )
        }

        Spacer(modifier = Modifier.weight(SetFieldRatio.OTHER))
    }
}