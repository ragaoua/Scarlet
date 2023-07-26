package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign

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
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    if(it.isFocused) {
                        tfValue = tfValue.copy(
                            selection = TextRange(0, tfValue.text.length)
                        )
                    } else {
                        tfValue = tfValue.copy(selection = TextRange(0, 0))
                        if (tfValue.text != originalValue) {
                            updateSet(tfValue.text)
                        }
                    }
                },
            value = tfValue,
            onValueChange = { tfValue = it },
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )
    }
}