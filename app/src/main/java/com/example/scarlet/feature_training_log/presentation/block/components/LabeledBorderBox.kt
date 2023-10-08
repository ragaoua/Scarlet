package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

/**
 * A box with a label on top of it.
 *
 * @param label The label to be displayed on top of the box.
 * @param border The border to be applied to the box.
 * @param shape The shape to be applied to the box.
 * @param content The content of the box.
 */
@Composable
fun LabeledBorderBox(
    label: @Composable () -> Unit,
    border: BorderStroke,
    shape: Shape,
    content: @Composable () -> Unit
) {
    val labelHorizontalPadding = 16.dp

    var labelCoordinates by remember { mutableStateOf<Rect?>(null) }
    var labelHeight by remember { mutableStateOf(0.dp) }
    var labelWidth by remember { mutableStateOf(0.dp) }

    Box(modifier = Modifier.wrapContentSize()) {
        val localDensity = LocalDensity.current
        Box(
            modifier = Modifier
                .sizeIn(minWidth = labelWidth + labelHorizontalPadding.times(2))
                .drawWithContent {
                    labelCoordinates?.let { coordinates ->
                        clipRect(
                            left = coordinates.left - 10,
                            top = coordinates.top,
                            right = coordinates.right + 10,
                            bottom = coordinates.bottom,
                            clipOp = ClipOp.Difference,
                        ) {
                            this@drawWithContent.drawContent()
                        }
                    } ?: drawContent()
                }
                .padding(top = labelHeight / 2)
                .border(
                    border = border,
                    shape = shape
                )
                .padding(top = labelHeight / 2)
        ) {
            content()
        }
        Box(
            modifier = Modifier
                .padding(horizontal = labelHorizontalPadding)
                .onGloballyPositioned {
                    labelCoordinates = it.boundsInParent()
                    labelHeight = with(localDensity) {
                        it.size.height.toDp()
                    }
                    labelWidth = with(localDensity) {
                        it.size.width.toDp()
                    }
                }
        ) {
            label()
        }
    }
}