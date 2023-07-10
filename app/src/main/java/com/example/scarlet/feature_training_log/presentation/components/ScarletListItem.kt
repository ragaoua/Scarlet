package com.example.scarlet.feature_training_log.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scarlet.R
import com.example.scarlet.ui.theme.Shapes

@Composable
fun ScarletListItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onDelete: (() -> Unit)? = null,
    colors: CardColors = CardDefaults.cardColors(),
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = colors,
        shape = Shapes.large
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
            onDelete?.let { onDelete ->
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete),
                    modifier = Modifier.clickable {
                        onDelete()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScarletListItem() {
    ScarletListItem(
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

@Preview(showBackground = true)
@Composable
fun PreviewScarletListItemDeletable() {
    ScarletListItem(
        onClick = {},
        onDelete = {},
        content = {
            Column {
                Text(
                    text = "Hello",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "World",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}