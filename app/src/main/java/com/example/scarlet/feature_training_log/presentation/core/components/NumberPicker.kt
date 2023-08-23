package com.example.scarlet.feature_training_log.presentation.core.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scarlet.R

/**
 * A number picker
 *
 * @param modifier Modifier to be applied to the picker
 * @param value The current value of the picker
 * @param onValueChange Callback to be invoked when the value changes
 * @param minValue The minimum value of the picker
 * @param maxValue The maximum value of the picker
 */
@Composable
fun NumberPicker(
    modifier: Modifier = Modifier,
    value: Int = 0,
    onValueChange: (Int) -> Unit,
    minValue: Int? = null,
    maxValue: Int? = null
) {
    Box(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { onValueChange(value - 1) },
                enabled = minValue == null || value > minValue
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = stringResource(R.string.decrement)
                )
            }

            Text(value.toString())

            IconButton(
                onClick = { onValueChange(value + 1) },
                enabled = maxValue == null || value < maxValue
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.increment)
                )
            }
        }
    }
}