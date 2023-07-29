package com.example.scarlet.feature_training_log.presentation.session.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.core.bottomBorder

@Composable
fun SetTextField(
    modifier: Modifier = Modifier,
    originalValue: String,
    onValueChangeCheck: (String) -> Boolean = { true },
    updateSet: (String) -> Unit,
    imeAction: ImeAction,
    onCopyPreviousSet: (() -> Unit)? = null
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
            onValueChange = {
                if (onValueChangeCheck(it.text)) {
                    tfValue = it
                }
            },
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Right
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            decorationBox = { innerTextField ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        onCopyPreviousSet?.let {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp, // TODO replace with ???
                                contentDescription = stringResource(R.string.copy_previous_set_value),
                                modifier = Modifier.clickable(onClick = onCopyPreviousSet)
                            )
                        } ?: Spacer(modifier = Modifier.width(24.dp))
                        Box(modifier = modifier.weight(1f)) {
                            innerTextField()
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                .bottomBorder(
                    strokeWidth = 1.dp,
                    color = MaterialTheme.colorScheme.primary
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