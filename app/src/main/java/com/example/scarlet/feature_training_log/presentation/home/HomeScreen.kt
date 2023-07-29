package com.example.scarlet.feature_training_log.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scarlet.R
import com.example.scarlet.feature_training_log.presentation.destinations.TrainingLogScreenDestination
import com.example.scarlet.ui.theme.ScarletTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator
) {
    ScarletTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically)
        ) {
            HomeScreenButton(
                text = stringResource(id = R.string.training_log),
                onClick = {
                    navigator.navigate(TrainingLogScreenDestination())
                }
            )
            HomeScreenButton(
                text = stringResource(id = R.string.statistics),
                onClick = { /* TODO */ }
            )
            HomeScreenButton (
                text = stringResource(id = R.string.competitions),
                onClick = { /* TODO */ }
            )
        }
    }
}

@Composable
fun HomeScreenButton(
    text: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .height(96.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Companion.BottomCenter
        ) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = text,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}