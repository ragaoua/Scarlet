package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onFocusChanged: (FocusState) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Box(
        modifier = modifier
    ) {
        TextField(
            modifier = Modifier
                .fillMaxSize()
                .onFocusChanged(onFocusChanged),
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            keyboardActions = KeyboardActions(
                onAny = { focusManager.clearFocus() }
            )
        )
    }
}