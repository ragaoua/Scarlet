package com.example.scarlet.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scarlet.R
import com.example.scarlet.viewmodel.TrainingLogViewModel
import com.example.scarlet.viewmodel.TrainingLogViewModelFactory

@Composable
fun ActiveBlockSection(
    //modifier: Modifier = Modifier,
    navController: NavController,
    factory: TrainingLogViewModelFactory,
    trainingLogViewModel: TrainingLogViewModel = viewModel(factory = factory)
) {
    val activeBlock by trainingLogViewModel.activeBlock.collectAsState(initial = null)

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.active_training_block),
            fontSize = 20.sp
        )
        activeBlock?.let { activeBlock ->
            BlockButton(
                navController = navController,
                block = activeBlock,
                factory = factory)
        }?: run {
            Text(text = "No active block")
        }
    }
}