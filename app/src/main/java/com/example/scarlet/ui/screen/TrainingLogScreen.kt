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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scarlet.R
import com.example.scarlet.db.ScarletDatabase
import com.example.scarlet.db.ScarletRepository
import com.example.scarlet.db.model.Block
import com.example.scarlet.ui.screen.destinations.BlockScreenDestination
import com.example.scarlet.ui.theme.ScarletTheme
import com.example.scarlet.viewmodel.TrainingLogViewModel
import com.example.scarlet.viewmodel.TrainingLogViewModelFactory
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun TrainingLogScreen(
    navigator: DestinationsNavigator
) {
    val factory = TrainingLogViewModelFactory(
        ScarletRepository(
            ScarletDatabase.getInstance(LocalContext.current)
        )
    )
    val trainingLogViewModel: TrainingLogViewModel = viewModel(factory = factory)
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
                ActiveBlockSection(navigator = navigator, trainingLogViewModel = trainingLogViewModel)
                CompletedBlocksSection(navigator = navigator, trainingLogViewModel = trainingLogViewModel)
            }
        }
    }
}

@Composable
fun ActiveBlockSection(
    navigator: DestinationsNavigator,
    trainingLogViewModel: TrainingLogViewModel
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
                navigator = navigator,
                block = activeBlock)
        }?: run {
            Text(text = "No active block")
        }
    }
}

@Composable
fun CompletedBlocksSection(
    navigator: DestinationsNavigator,
    trainingLogViewModel: TrainingLogViewModel
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
                navigator = navigator,
                block = block)
        }
    }
}

@Composable
fun BlockButton(
    navigator: DestinationsNavigator,
    block: Block
) {
    Button(onClick = {
        navigator.navigate(BlockScreenDestination(block = block))
    }) {
        Text(block.name)
    }
}