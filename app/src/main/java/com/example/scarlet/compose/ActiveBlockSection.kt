package com.example.scarlet.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scarlet.R
import com.example.scarlet.viewmodel.TrainingLogViewModel

@Composable
fun ActiveBlockSection(
    //modifier: Modifier = Modifier,
    trainingLogViewModel: TrainingLogViewModel = viewModel()
) {
    val activeBlock = trainingLogViewModel.getActiveBlock()
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.active_training_block),
            fontSize = 20.sp
        )
        if (activeBlock != null) {
            Button(onClick = {
                /* TODO */
            }) {
                Text(activeBlock.name!!)
            }
        }
        else {
            Button(onClick = {
                /* TODO */
            }) {
                Text(stringResource(R.string.no_active_block_msg))
            }
        }

    }

}