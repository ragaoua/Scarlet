package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.scarlet.feature_training_log.presentation.block.util.SetFieldType
import com.example.scarlet.feature_training_log.presentation.session.SessionEvent
import com.example.scarlet.feature_training_log.presentation.session.util.SetFieldRatio

@Composable
fun ExerciseSetRow(
    set: Set,
    isFirstSet: Boolean,
    isLastSet: Boolean,
    onEvent: (SessionEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(SetFieldRatio.SET)) {
            Text(
                text = "${set.order}.",
                modifier = Modifier.align(Alignment.Center)
            )
        }

        SetTextField(
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .weight(SetFieldRatio.REPS),
            originalValue = set.reps?.toString() ?: "",
            onValueChangeCheck = { text ->
                if (text.isBlank()) true else {
                    text.toIntOrNull()?.let { reps ->
                        reps < 1000
                    } ?: false // TODO : validate in a use case
                }
            },
            updateSet = { text ->
                onEvent(SessionEvent.UpdateSet(
                    set.copy(reps = text.toIntOrNull())
                ))
            },
            imeAction = ImeAction.Next,
            onIconClicked = if (isFirstSet) null else {
                {
                    onEvent(SessionEvent.CopyPreviousSet(
                        set = set,
                        fieldToCopy = SetFieldType.REPS
                    ))
                }
            }
        )

        SetTextField(
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .weight(SetFieldRatio.WEIGHT),
            originalValue = set.weight?.toString() ?: "",
            onValueChangeCheck = { text ->
                if (text.isBlank()) true else {
                    text.toFloatOrNull()?.let { weight ->
                        weight < 1000
                    } ?: false // TODO : validate in a use case
                }
            },
            updateSet = { text ->
                onEvent(SessionEvent.UpdateSet(
                    set.copy(weight = text.toFloatOrNull())
                ))
            },
            imeAction = ImeAction.Next,
            onIconClicked = if (isFirstSet) null else {
                {
                    onEvent(SessionEvent.CopyPreviousSet(
                        set = set,
                        fieldToCopy = SetFieldType.WEIGHT
                    ))
                }
            },
            onIconLongClicked = {
                onEvent(SessionEvent.ShowLoadCalculationDialog(set))
            }
        )

        SetTextField(
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .weight(SetFieldRatio.RPE),
            originalValue = set.rpe?.toString() ?: "",
            onValueChangeCheck = { text ->
                if (text.isBlank()) true else {
                    text.toFloatOrNull()?.let { rpe ->
                        rpe <= 10
                    } ?: false // TODO : validate in a use case
                }
            },
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
                .weight(SetFieldRatio.OTHER)
                .clickable {
                    onEvent(SessionEvent.DeleteSet(set))
                }
        )
    }
}