package com.example.scarlet.feature_training_log.presentation.block.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.presentation.block.BlockEvent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockHeader(
    block: Block,
    isEditing: Boolean,
    onEvent: (BlockEvent) -> Unit
){
    val editIconPadding = 24.dp

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {

            if(isEditing) {
                val blockName = remember(block.name) { mutableStateOf(block.name) }
                val focusManager = LocalFocusManager.current

                TextField(
                    modifier = Modifier.onFocusChanged {
                        if (!it.isFocused &&
                            block.name.isNotBlank() &&
                            blockName.value != block.name)
                        {
                            onEvent(BlockEvent.UpdateBlock(
                                block.copy(name = blockName.value)
                            ))
                        }
                    },
                    value = blockName.value,
                    onValueChange = { blockName.value = it },
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    singleLine = true,
                    keyboardActions = KeyboardActions(
                        onAny = { focusManager.clearFocus() }
                    )
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_block_name),
                    modifier = Modifier
                        .size(editIconPadding)
                        .align(Alignment.CenterEnd)
                        .clickable { onEvent(BlockEvent.EditBlockClicked) }
                )
                Text(
                    text = block.name,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .padding(horizontal = editIconPadding + 4.dp)
                        .align(Alignment.Center)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Divider(
            modifier = Modifier.width(64.dp),
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(onClick = {
                onEvent(BlockEvent.AddSession)
            }) {
                Text(text = stringResource(id = R.string.new_session))
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = {
                    onEvent(BlockEvent.EndBlock)
                },
                enabled = !block.completed
            ) {
                Text(text = stringResource(id = R.string.end_block))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    BlockHeader(
        block = Block(name = "Block 1"),
        isEditing = false,
        onEvent = {}
    )
}