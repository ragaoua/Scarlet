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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scarlet.R
import com.example.scarlet.db.model.Session
import com.example.scarlet.ui.navigation.Screen
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
                    blockName = block!!.name
                )
                SessionsSection(
                    sessions = blockSessions,
                    navController = navController
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
    navController: NavController
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        sessions.forEach { session ->
            Button(onClick = {
                navController.navigate(Screen.SessionScreen.withId(session.id))
            }) {
                Text(session.date)
            }
        }
    }
}