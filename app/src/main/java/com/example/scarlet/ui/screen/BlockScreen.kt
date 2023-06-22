package com.example.scarlet.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import com.example.scarlet.viewmodel.TrainingLogOldViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun BlockScreen(
    navigator: DestinationsNavigator,
    block: Block
) {
    val trainingLogOldViewModel: TrainingLogOldViewModel = hiltViewModel()

    val sessions by trainingLogOldViewModel.getSessionsByBlockId(block.id).collectAsState(initial = null)
    ScarletTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            BlockHeader(
                block = block,
                trainingLogOldViewModel = trainingLogOldViewModel
            )
            sessions?.let { sessions ->
                SessionsSection(
                    sessions = sessions,
                    navigator = navigator
                )
            }
        }
    }
}

@Composable
fun BlockHeader(
    block: Block,
    trainingLogOldViewModel: TrainingLogOldViewModel
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = block.name,
            fontSize = 20.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                trainingLogOldViewModel.addSession(block)
            }) {
                Text(text = stringResource(id = R.string.new_session))
            }
            /* TODO : check if the block is already completed */
            Button(onClick = {
                trainingLogOldViewModel.endBlock(block)
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