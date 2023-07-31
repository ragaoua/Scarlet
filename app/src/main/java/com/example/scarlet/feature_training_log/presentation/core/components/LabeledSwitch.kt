package com.example.scarlet.feature_training_log.presentation.core.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun LabeledSwitch(
    modifier: Modifier = Modifier,
    lText: String,
    rText: String,
    initialSelection: LabeledSwitchSelection = LabeledSwitchSelection.LEFT,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    selectionColor: Color = MaterialTheme.colorScheme.primary,
    shape: Shape = MaterialTheme.shapes.small
) {
    var selection by remember { mutableStateOf(initialSelection) }

    Row(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .clickable {
                selection =
                    if (selection == LabeledSwitchSelection.LEFT) LabeledSwitchSelection.RIGHT
                    else LabeledSwitchSelection.LEFT
            }
    ) {
        SwitchItem(
            text = lText,
            isSelected = selection == LabeledSwitchSelection.LEFT,
            selectionColor = selectionColor,
            shape = shape
        )
        SwitchItem(
            text = rText,
            isSelected = selection == LabeledSwitchSelection.RIGHT,
            selectionColor = selectionColor,
            shape = shape
        )
    }
}

@Composable
private fun SwitchItem(
    text: String,
    isSelected: Boolean,
    selectionColor: Color,
    shape: Shape
) {
    Box {
        AnimatedVisibility(
            visible = isSelected,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.matchParentSize()
        ) {
            Box(
                modifier = Modifier
                    .clip(shape)
                    .background(selectionColor)
            )
        }
        Text(
            text = text,
            color = Color.White, // TODO calculate from "selected"
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

enum class LabeledSwitchSelection {
    LEFT, RIGHT
}