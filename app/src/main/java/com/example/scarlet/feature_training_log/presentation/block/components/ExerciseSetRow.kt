package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Set
import com.example.scarlet.feature_training_log.presentation.block.BlockEvent
import com.example.scarlet.feature_training_log.presentation.block.util.SetFieldRatio
import com.example.scarlet.feature_training_log.presentation.block.util.SetFieldType

/**
 * Row containing the fields of a set
 *
 * @param modifier modifier for the row
 * @param set set to be displayed
 * @param isCopyRepsIconVisible whether the copy icon should be visible for the reps field
 * @param isCopyLoadIconVisible whether the copy icon should be visible for the load field
 * @param isLastSet whether the set is the last one of the exercise. This is used to determine
 * the ime action of the last field
 * @param onEvent event to be triggered when the user interacts with the row
 *
 * @see SetField
 */
@Composable
fun ExerciseSetRow(
    modifier: Modifier = Modifier,
    set: Set,
    selectedSet: Set? = null,
    selectedSetField: SetFieldType? = null,
    isCopyRepsIconVisible: Boolean,
    isCopyLoadIconVisible: Boolean,
    isLastSet: Boolean,
    onEvent: (BlockEvent) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(SetFieldRatio.SET),
            text = "${set.order}.",
            style = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
        )

        SetField(
            modifier = Modifier.weight(SetFieldRatio.REPS),
            text = set.reps?.toString() ?: "",
            isSelected = selectedSet == set && selectedSetField == SetFieldType.REPS,
            onClick = { onEvent(BlockEvent.ShowSetTextField(set, SetFieldType.REPS)) },
            onIconClicked = if (isCopyRepsIconVisible) {
                {
                    onEvent(BlockEvent.CopyPreviousSet(
                        set = set,
                        fieldToCopy = SetFieldType.REPS
                    ))
                }
            } else null
        )

        SetField(
            modifier = Modifier.weight(SetFieldRatio.LOAD),
            text = set.load?.let {
                if (it % 1 == 0f) {
                    it.toInt().toString()
                } else it.toString()
            } ?: "",
            isSelected = selectedSet == set && selectedSetField == SetFieldType.LOAD,
            onClick = { onEvent(BlockEvent.ShowSetTextField(set, SetFieldType.LOAD)) },
            onIconClicked = if (isCopyLoadIconVisible) {
                {
                    onEvent(BlockEvent.CopyPreviousSet(
                        set = set,
                        fieldToCopy = SetFieldType.LOAD
                    ))
                }
            } else null,
            onIconLongClicked = { onEvent(BlockEvent.ShowLoadCalculationDialog(set)) }
        )

        SetField(
            modifier = Modifier.weight(SetFieldRatio.RATING),
            text = set.rating?.let {
                if (it % 1 == 0f) {
                    it.toInt().toString()
                } else it.toString()
            } ?: "",
            isSelected = selectedSet == set && selectedSetField == SetFieldType.RATING,
            onClick = { onEvent(BlockEvent.ShowSetTextField(set, SetFieldType.RATING)) }
        )

        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(R.string.delete),
            modifier = Modifier
                .weight(SetFieldRatio.OTHER)
                .clickable { onEvent(BlockEvent.DeleteSet(set)) }
        )
    }
}