package com.example.scarlet.feature_training_log.presentation.core.components

import androidx.compose.foundation.layout.Arrangement
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
 * A generic number picker
 *
 * @param modifier Modifier to be applied to the picker
 * @param value The string representation of the value of the picker
 * @param onMinusClick Callback when the minus button is clicked
 * @param onPlusClick Callback when the plus button is clicked
 * @param isMinusButtonEnabled Whether the minus button is enabled
 * @param isPlusButtonEnabled Whether the plus button is enabled
 */
@Composable
fun NumberPicker(
    modifier: Modifier = Modifier,
    value: String,
    onMinusClick: (String) -> Unit,
    onPlusClick: (String) -> Unit,
    isMinusButtonEnabled: Boolean = true,
    isPlusButtonEnabled: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = { onMinusClick(value) },
            enabled = isMinusButtonEnabled
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = stringResource(R.string.decrement)
            )
        }

        Text(value)

        IconButton(
            onClick = { onPlusClick(value) },
            enabled = isPlusButtonEnabled
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.increment)
            )
        }
    }
}

/**
 * A whole number picker.
 * When the plus or minus button is clicked, the value is incremented or decremented by 1
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
    NumberPicker(
        modifier = modifier,
        value = value.toString(),
        onMinusClick = {
            onValueChange(it.toInt() - 1)
        },
        onPlusClick = {
            onValueChange(it.toInt() + 1)
        },
        isMinusButtonEnabled = minValue == null || value > minValue,
        isPlusButtonEnabled = maxValue == null || value < maxValue
    )
}