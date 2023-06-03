package com.example.scarlet.compose

import android.content.Context
import android.content.Intent
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
import com.example.scarlet.activities.BlockActivity
import com.example.scarlet.viewmodel.TrainingLogViewModel
import com.example.scarlet.viewmodel.TrainingLogViewModelFactory

@Composable
fun ActiveBlockSection(
    //modifier: Modifier = Modifier,
    context: Context,
    factory: TrainingLogViewModelFactory,
    trainingLogViewModel: TrainingLogViewModel = viewModel(factory = factory)
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
                val intent = Intent(context, BlockActivity::class.java)
                intent.putExtra("block", activeBlock)
                context.startActivity(intent)
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