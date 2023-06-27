package com.example.scarlet.feature_training_log.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SectionTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Box(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = title,
            style = MaterialTheme.typography.titleLarge
        )
    }
}