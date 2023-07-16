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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.presentation.block.BlockEvent


@Composable
fun BlockHeader(
    block: Block,
    isEditing: Boolean,
    onEvent: (BlockEvent) -> Unit
){
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(isEditing) {
            SideEffect {
                focusRequester.requestFocus()
            }
            val blockName = remember(block.name) { mutableStateOf(block.name) }

            OutlinedTextField(
                modifier = Modifier.focusRequester(focusRequester),
                value = blockName.value,
                onValueChange = { blockName.value = it },
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    textAlign = TextAlign.Center,
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.save_block_name),
                        modifier = Modifier.clickable {
                            onEvent(BlockEvent.UpdateBlock(
                                block.copy(name = blockName.value)
                            ))
                        }
                    )
                },
                keyboardActions = KeyboardActions (
                    onDone = {
                        onEvent(BlockEvent.UpdateBlock(
                            block.copy(name = blockName.value)
                        ))
                    }
                ),
            )
        } else {
            Box {
                val editIconPadding = 24.dp

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_block_name),
                    modifier = Modifier
                        .size(editIconPadding)
                        .align(Alignment.CenterEnd)
                        .clickable { onEvent(BlockEvent.EditBlock) }
                )
                Text(
                    text = block.name,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .padding(horizontal = editIconPadding + 4.dp)
                        .align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Divider(
                modifier = Modifier.width(64.dp),
                thickness = 1.dp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(
                onClick = {
                    onEvent(BlockEvent.AddSession)
                },
                enabled = !isEditing
            ) {
                Text(text = stringResource(id = R.string.new_session))
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = {
                    onEvent(BlockEvent.EndBlock)
                },
                enabled = !block.completed && !isEditing
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