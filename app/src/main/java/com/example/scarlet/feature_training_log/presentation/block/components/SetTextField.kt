package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.scarlet.R
import com.example.scarlet.core.util.conditional
import com.example.scarlet.feature_training_log.presentation.core.bottomBorder
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun SetTextField(
    modifier: Modifier = Modifier,
    originalValue: String,
    onValueChangeCheck: (String) -> Boolean = { true },
    updateSet: (String) -> Unit,
    imeAction: ImeAction,
    onIconClicked: (() -> Unit)? = null,
    onIconLongClicked: (() -> Unit)? = null
) {
    var tfValue by remember(originalValue) { mutableStateOf(
        TextFieldValue(originalValue)
    )}

    val focusManager = LocalFocusManager.current
    val isImeVisible = WindowInsets.isImeVisible
    LaunchedEffect(isImeVisible) {
        // Make sure to clear focus when the keyboard is discarded
        // since the system doesn't do it automatically
        if (!isImeVisible) {
            focusManager.clearFocus()
        }
    }

    var isFocused by remember { mutableStateOf(false) }

    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = modifier) {
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
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    onIconClicked?.let {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp, // TODO replace with ???
                            contentDescription = stringResource(R.string.copy_previous_set_value),
                            modifier = Modifier.combinedClickable(
                                onClick = onIconClicked,
                                onLongClick = onIconLongClicked
                            )
                        )
                    } ?: Spacer(modifier = Modifier.width(24.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        innerTextField()
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }
            },
            modifier = Modifier
                .bringIntoViewRequester(bringIntoViewRequester)
                .conditional(isFocused) {
                    onGloballyPositioned {
                        coroutineScope.launch {
                            // Bring this set into view and add
                            // some extra space at the bottom.
                            // 3 times the height of the set
                            // seems to be a good value.
                            bringIntoViewRequester.bringIntoView(
                                Rect(
                                    offset = Offset.Zero,
                                    size = IntSize(
                                        width = it.size.width,
                                        height = 3*it.size.height
                                    ).toSize()
                                )
                            )
                        }
                    }
                }
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .bottomBorder(
                    strokeWidth = 1.dp,
                    color = MaterialTheme.colorScheme.primary
                )
                .onFocusChanged {
                    isFocused = it.isFocused
                    if (it.isFocused) {
                        tfValue = tfValue.copy(
                            selection = TextRange(0, tfValue.text.length)
                        )
                    } else {
                        // Manually clear the selection. Otherwise, the
                        // focus will be lost but the selection will remain
                        tfValue = tfValue.copy(selection = TextRange(0, 0))
                        if (tfValue.text != originalValue) {
                            updateSet(tfValue.text)
                        }
                    }
                }
        )
    }
}