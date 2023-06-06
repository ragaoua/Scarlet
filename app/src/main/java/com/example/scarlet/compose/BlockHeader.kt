package com.example.scarlet.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scarlet.R
import com.example.scarlet.viewmodel.TrainingLogViewModel
import com.example.scarlet.viewmodel.TrainingLogViewModelFactory

@Composable
fun BlockHeader(
    blockName: String,
    navController: NavController,
    factory: TrainingLogViewModelFactory,
    trainingLogViewModel: TrainingLogViewModel = viewModel(factory = factory)
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = blockName,
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