package com.example.scarlet.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scarlet.R
import com.example.scarlet.db.model.Block
import com.example.scarlet.ui.navigation.Screen
import com.example.scarlet.ui.theme.ScarletTheme
import com.example.scarlet.viewmodel.TrainingLogViewModel
import com.example.scarlet.viewmodel.TrainingLogViewModelFactory

@Composable
fun TrainingLogScreen(
    navController: NavController,
    factory: TrainingLogViewModelFactory
) {
    ScarletTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.training_log),
                    fontSize = 48.sp
                )
                ActiveBlockSection(navController = navController, factory = factory)
                CompletedBlocksSection(navController = navController, factory = factory)
            }
        }
    }
}

@Composable
fun ActiveBlockSection(
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
                block = activeBlock)
        }?: run {
            Text(text = "No active block")
        }
    }
}

@Composable
fun CompletedBlocksSection(
    navController: NavController,
    factory: TrainingLogViewModelFactory,
    trainingLogViewModel: TrainingLogViewModel = viewModel(factory = factory)
) {
    val completedBlocks by trainingLogViewModel.completedBlocks.collectAsState(initial = emptyList())
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.completed_training_blocks),
            fontSize = 20.sp
        )
        completedBlocks.forEach {block ->
            BlockButton(
                navController = navController,
                block = block)
        }
    }
}

@Composable
fun BlockButton(
    navController: NavController,
    block: Block
) {
    Button(onClick = {
        navController.navigate(Screen.BlockScreen.withId(block.id))
    }) {
        Text(block.name)
    }
}