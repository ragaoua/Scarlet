package com.example.scarlet.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.scarlet.R
import com.example.scarlet.ui.navigation.Screen
import com.example.scarlet.ui.theme.ScarletTheme

@Composable
fun HomeScreen(
    navController: NavController
) {
    ScarletTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        navController.navigate(Screen.TrainingLogScreen.route)
                    }) {
                    Text(stringResource(id = R.string.training_log))
                }
                Button(onClick = {
                    /*TODO*/
                }) {
                    Text(stringResource(id = R.string.statistics))
                }
                Button(onClick = {
                    /*TODO*/
                }) {
                    Text(stringResource(id = R.string.competitions))
                }
            }
        }
    }
}