package com.example.scarlet.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun <T> ScarletList(
    modifier: Modifier = Modifier,
    title: String,
    items: List<T>,
    onItemClicked: (item: T) -> Unit = {},
    onDeleteClicked: ((item: T) -> Unit)? = null,
    itemContent: @Composable (item: T) -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { item ->
                ScarletListItem(
                    onClick = { onItemClicked(item) },
                    onDelete =
                        onDeleteClicked?.let {
                            { onDeleteClicked(item) }
                        }
                ) {
                    itemContent(item)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewScarletList() {
    ScarletList(
        title = "Hello World",
        items = listOf("Hello", "World"),
        itemContent = { item ->
            Text(text = item)
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewScarletListDeletable() {
    ScarletList(
        title = "Hello World",
        items = listOf("Hello", "World"),
        onDeleteClicked = {},
        itemContent = { item ->
            Text(text = item)
        }
    )
}

