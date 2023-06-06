package com.example.scarlet.compose

import android.content.Context
import android.content.Intent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scarlet.activities.BlockActivity
import com.example.scarlet.model.Block
import com.example.scarlet.viewmodel.TrainingLogViewModel
import com.example.scarlet.viewmodel.TrainingLogViewModelFactory

@Composable
fun BlockButton(
    context: Context,
    block: Block,
    factory: TrainingLogViewModelFactory,
    trainingLogViewModel: TrainingLogViewModel = viewModel(factory = factory)
) {
    Button(onClick = {
        trainingLogViewModel.setDisplayedBlock(block)
        /* TODO use NavController */
        val intent = Intent(context, BlockActivity::class.java)
        intent.putExtra("block", block)
        context.startActivity(intent)
    }) {
        Text(block.name!!)
    }
}