package com.example.scarlet.feature_training_log.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scarlet.R

/**
 * Displays a "Delete" clickable icon on the far right of a content
 * Horizontal padding is added to the content to avoid the icon overlapping it
 */
@Composable
fun DeletableItem(
    modifier: Modifier = Modifier,
    onDeleteClicked: () -> Unit,
    iconSize: Dp = 32.dp,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(R.string.delete),
            modifier = Modifier
                .size(iconSize)
                .align(Alignment.CenterEnd)
                .clickable(onClick = onDeleteClicked)
        )
        Box(modifier = Modifier.padding(horizontal = iconSize)) {
            content()
        }
    }
}