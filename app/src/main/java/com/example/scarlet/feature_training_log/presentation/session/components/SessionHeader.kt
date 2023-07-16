package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scarlet.feature_training_log.presentation.session.SessionUiState

@Composable
fun SessionHeader(
    state: SessionUiState
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = state.sessionBlockName, /* TODO */
            style = MaterialTheme.typography.headlineSmall,
            // TODO : color = grey
        )
        Spacer(modifier = Modifier.height(4.dp))
        Divider(
            modifier = Modifier.width(64.dp),
            thickness = 1.dp
        )
        Text(
            text = state.session.date,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}