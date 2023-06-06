package com.example.scarlet.compose

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
import com.example.scarlet.activities.TrainingLogActivity

@Composable
fun HomeScreen(context: Context){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                /* TODO : use NavController */
                val intent = Intent(context, TrainingLogActivity::class.java)
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