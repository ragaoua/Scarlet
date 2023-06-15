package com.example.scarlet.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scarlet.R
import com.example.scarlet.db.model.Block
import com.example.scarlet.db.model.Session
import com.example.scarlet.ui.screen.destinations.SessionScreenDestination
import com.example.scarlet.ui.theme.ScarletTheme
import com.example.scarlet.viewmodel.TrainingLogViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun BlockScreen(
    navigator: DestinationsNavigator,
    block: Block
) {
    val trainingLogViewModel: TrainingLogViewModel = hiltViewModel()

    val blockWithSessions by trainingLogViewModel.getBlockWithSessionsById(block.id).collectAsState(initial = null)
    ScarletTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            blockWithSessions?.let { blockWithSessions ->
                BlockHeader(
                    blockName = blockWithSessions.block.name
                )
                SessionsSection(
                    sessions = blockWithSessions.sessions,
                    navigator = navigator
                )
            }
        }
    }
}

@Composable
fun BlockHeader(
    blockName: String
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

@Composable
fun SessionsSection(
    sessions: List<Session>,
    navigator: DestinationsNavigator
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        sessions.forEach { session ->
            Button(onClick = {
                navigator.navigate(SessionScreenDestination(session = session))
            }) {
                Text(session.date)
            }
        }
    }
}