package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Exercise
import com.example.scarlet.feature_training_log.presentation.components.SecondaryActionButton
import com.example.scarlet.feature_training_log.presentation.session.SessionEvent

@Composable
fun AddSetButton(
    exercise: Exercise,
    onEvent: (SessionEvent) -> Unit
) {
    SecondaryActionButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        onClick = {
            onEvent(SessionEvent.NewSet(exercise))
        }
    ) {
       Icon(
           imageVector = Icons.Default.Add,
           contentDescription = stringResource(R.string.add_set)
       )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddSetButton() {
    AddSetButton(
        exercise = Exercise(),
        onEvent = {}
    )
}