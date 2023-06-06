package com.example.scarlet.compose

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scarlet.Screen
import com.example.scarlet.model.Block
import com.example.scarlet.viewmodel.TrainingLogViewModel
import com.example.scarlet.viewmodel.TrainingLogViewModelFactory

@Composable
fun BlockButton(
    navController: NavController,
    block: Block,
    factory: TrainingLogViewModelFactory,
    trainingLogViewModel: TrainingLogViewModel = viewModel(factory = factory)
) {
    Button(onClick = {
        navController.navigate(Screen.BlockScreen.withId(block.id))
    }) {
        Text(block.name)
    }
}