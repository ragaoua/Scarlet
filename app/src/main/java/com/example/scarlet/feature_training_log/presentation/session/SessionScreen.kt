package com.example.scarlet.feature_training_log.presentation.session

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.session.components.ExercisesList
import com.example.scarlet.feature_training_log.presentation.session.components.LoadCalculationDialog
import com.example.scarlet.feature_training_log.presentation.session.components.MovementSelectionSheet
import com.example.scarlet.feature_training_log.presentation.session.components.SessionHeader
import com.example.scarlet.ui.theme.ScarletTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(
    navArgsDelegate = SessionScreenNavArgs::class
)
@Composable
fun SessionScreen(
    navigator: DestinationsNavigator
) {
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val state by sessionViewModel.state.collectAsState()

    Screen(
        state = state,
        onEvent = sessionViewModel::onEvent
    )
}

@Composable
fun Screen(
    state: SessionUiState,
    onEvent: (SessionEvent) -> Unit
) {
    ScarletTheme {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        onEvent(SessionEvent.ToggleEditMode)
                    },
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    if (state.isInEditMode) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.save_session)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.edit_session)
                        )
                    }
                }
            }
        ) { contentPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    SessionHeader(
                        state = state,
                        onEvent = onEvent
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    ExercisesList(
                        state = state,
                        onEvent = onEvent
                    )
                }
            }
        }
        if(state.isMovementSelectionSheetOpen) {
            MovementSelectionSheet(
                movements = state.movements,
                onEvent = onEvent
            )
        }
        state.loadCalculationDialogState?.let {
            LoadCalculationDialog(
                dialogState = it,
                onEvent = onEvent
            )
        }
    }
}