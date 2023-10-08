package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp

@Composable
fun NumberedCheckBox(
    modifier: Modifier = Modifier,
    number: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val checkBoxSize = 40.dp
    val checkBoxPadding = 10.dp
    val borderWidth = 2.dp

    Box(
        modifier = modifier
            .size(checkBoxSize)
            .clip(RoundedCornerShape(100))
            .triStateToggleable(
                state = ToggleableState(checked),
                onClick = { onCheckedChange(!checked) },
                role = Role.Checkbox
            ).padding(checkBoxPadding)
            .clip(RoundedCornerShape(10))
            .then(
                if (!checked) {
                    Modifier.border(
                        BorderStroke(
                            width = borderWidth,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                } else Modifier.background(MaterialTheme.colorScheme.primary)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}