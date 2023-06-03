package com.example.scarlet.activities

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.example.scarlet.R

@Composable
fun HomeScreen(context: Context){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                val intent = Intent(context, TrainingLogsActivity2::class.java)
                context.startActivity(intent)
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