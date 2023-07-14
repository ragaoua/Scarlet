package com.example.scarlet.feature_training_log.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scarlet.R

/**
 * Displays a "Delete" clickable on the far right of a content
 */
@Composable
fun DeletableItem(
    modifier: Modifier = Modifier,
    onDeleteClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(R.string.delete),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable(onClick = onDeleteClicked)
        )
        content()
    }
}