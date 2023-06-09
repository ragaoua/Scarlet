package com.example.scarlet.feature_training_log.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScarletListItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onDelete: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
            /* TODO */
//            onDelete?.let { onDelete ->
//                Spacer(modifier = Modifier.weight(1f))
//                Icon(
//                    imageVector = Icons.Default.Delete,
//                    contentDescription = stringResource(R.string.delete),
//                    modifier = Modifier.clickable {
//                        onDelete()
//                    }
//                )
//            }
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