package com.example.scarlet.feature_training_log.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    Box(
        modifier = modifier
    ) {
        Column {
            ScarletListTitle(title = title)
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
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

