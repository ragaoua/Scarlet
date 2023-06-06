package com.example.scarlet.compose

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scarlet.R
import com.example.scarlet.viewmodel.TrainingLogViewModel
import com.example.scarlet.viewmodel.TrainingLogViewModelFactory

@Composable
fun BlockHeader(
    context: Context,
    factory: TrainingLogViewModelFactory,
    trainingLogViewModel: TrainingLogViewModel = viewModel(factory = factory)
){
    val block by trainingLogViewModel.displayedBlock.collectAsState(initial = null)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = block?.let { block -> block.name!! }?: run { "" } ,
            fontSize = 20.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                /* TODO */
            }) {
                Text(text = stringResource(id = R.string.new_session))
            }
            Button(onClick = {
                /* TODO */
            }) {
                Text(text = stringResource(id = R.string.end_block))
            }
        }
    }
}