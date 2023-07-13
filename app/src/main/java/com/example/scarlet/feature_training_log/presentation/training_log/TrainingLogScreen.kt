package com.example.scarlet.feature_training_log.presentation.training_log

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.domain.model.Block
import com.example.scarlet.feature_training_log.domain.model.BlockWithDates
import com.example.scarlet.feature_training_log.presentation.training_log.components.ActiveBlockSection
import com.example.scarlet.feature_training_log.presentation.training_log.components.CompletedBlocksSection
import com.example.scarlet.feature_training_log.presentation.training_log.components.NewBlockDialog
import com.example.scarlet.ui.theme.ScarletTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Destination
@Composable
fun TrainingLogScreen(
    navigator: DestinationsNavigator
) {
    val trainingLogViewModel: TrainingLogViewModel = hiltViewModel()
    val state by trainingLogViewModel.state.collectAsState()

    Screen(
        navigator = navigator,
        state = state,
        onEvent = trainingLogViewModel::onEvent
    )
}

@Composable
fun Screen(
    navigator: DestinationsNavigator,
    state: TrainingLogUiState,
    onEvent: (TrainingLogEvent) -> Unit
) {
    ScarletTheme {
        if(state.isAddingBlock) {
            NewBlockDialog(
                state = state,
                onEvent = onEvent
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.training_log),
                fontSize = 48.sp, /* TODO : make this a resource */
                modifier = Modifier.padding(top = 32.dp, bottom = 32.dp)
            ) // TODO : make into a "screen title" function ?
            ActiveBlockSection(
                navigator = navigator,
                activeBlock = state.activeBlock,
                onEvent = onEvent
            )
            Spacer(modifier = Modifier.height(16.dp))
            CompletedBlocksSection(
                navigator = navigator,
                completedBlocks = state.completedBlocks,
                onEvent = onEvent
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewNoBlocks() {
    Screen(
        navigator = EmptyDestinationsNavigator,
        state = TrainingLogUiState(),
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTrainingLogScreen() {
    Screen(
        navigator = EmptyDestinationsNavigator,
        state = TrainingLogUiState(
            activeBlock =
                BlockWithDates(
                    block = Block(name = "Block 5"),
                    firstSessionDate = "2021-08-01",
                    lastSessionDate = "2021-08-05"
                ),
            completedBlocks = listOf(
                BlockWithDates(
                    block = Block(name = "Block 1"),
                    firstSessionDate = "01/01/2021",
                    lastSessionDate = "01/02/2021"
                ),
                BlockWithDates(
                    block = Block(name = "Block 2"),
                    firstSessionDate = "01/01/2021",
                    lastSessionDate = "01/02/2021"
                )
            )
        ),
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewNewBlockDialog() {
    Screen(
        navigator = EmptyDestinationsNavigator,
        state = TrainingLogUiState(
            isAddingBlock = true
        ),
        onEvent = {}
    )
}