package com.example.scarlet.ui.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scarlet.ui.theme.ScarletTheme
import com.example.scarlet.viewmodel.TrainingLogViewModel
import com.example.scarlet.viewmodel.TrainingLogViewModelFactory

@Composable
fun BlockScreen(
    blockId: Int,
    navController: NavController,
    factory: TrainingLogViewModelFactory,
    trainingLogViewModel: TrainingLogViewModel = viewModel(factory = factory)
) {
    val block by trainingLogViewModel.getBlockById(blockId).collectAsState(initial = null)
    val blockSessions by trainingLogViewModel.getSessionsByBlockId(blockId).collectAsState(initial = emptyList())
    ScarletTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            block?.let {
                BlockHeader(
                    blockName = block!!.name,
                    navController = navController,
                    factory = factory
                )
                SessionsSection(
                    sessions = blockSessions,
                    navController = navController,
                    factory = factory
                )
            }
        }
    }
}