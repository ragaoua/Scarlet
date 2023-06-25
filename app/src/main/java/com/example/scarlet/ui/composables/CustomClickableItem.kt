package com.example.scarlet.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScarletClickableItem(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface))
            .padding(8.dp)
            .clickable {
                onClick()
            }
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomClickableItem() {
    ScarletClickableItem(
        onClick = {},
        content = {
            Text(
                text = "Hello World",
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
    )
}