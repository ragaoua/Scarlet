package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.scarlet.R
import com.example.scarlet.core.util.conditional
import com.example.scarlet.feature_training_log.presentation.core.bottomBorder
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SetField(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    onIconClicked: (() -> Unit)? = null,
    onIconLongClicked: (() -> Unit)? = null
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    Row(
        modifier = modifier
            .bringIntoViewRequester(bringIntoViewRequester)
            .conditional(isSelected) {
                onGloballyPositioned {
                    coroutineScope.launch {
                        bringIntoViewRequester.bringIntoView(
                            // Bring this set into view and add
                            // some extra space at the bottom.
                            // 3 times the height of the set
                            // seems to be a good value.
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
            .padding(horizontal = 2.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Modifier.bottomBorder(
                        strokeWidth = 1.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            )
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        onIconClicked?.let {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp, // TODO replace with ???
                contentDescription = stringResource(R.string.copy_previous_set_value),
                modifier = Modifier.combinedClickable(
                    onClick = it,
                    onLongClick = onIconLongClicked
                )
            )
        } ?: Spacer(modifier = Modifier.width(24.dp)) // Default icon size
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            style = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Right
            ),
            maxLines = 1
        )
    }
}