package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SetTextField(
    modifier: Modifier = Modifier,
    originalValue: String,
    updateSet: (String) -> Unit,
    imeAction: ImeAction
) {
    var tfValue by remember(originalValue) { mutableStateOf(
        TextFieldValue(originalValue)
    )}

    val focusManager = LocalFocusManager.current
    Box(
        modifier = modifier
    ) {
        BasicTextField(
            value = tfValue,
            onValueChange = { tfValue = it },
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            decorationBox = { innerTextField ->
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(4.dp))
                    innerTextField()
                    Spacer(modifier = Modifier.height(4.dp))
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surface,
                    MaterialTheme.shapes.small,
                )
                .onFocusChanged {
                    if (it.isFocused) {
                        tfValue = tfValue.copy(
                            selection = TextRange(0, tfValue.text.length)
                        )
                    } else {
                        tfValue = tfValue.copy(selection = TextRange(0, 0))
                        if (tfValue.text != originalValue) {
                            updateSet(tfValue.text)
                        }
                    }
                }
        )
    }
}