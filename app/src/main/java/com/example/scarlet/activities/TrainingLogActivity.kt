package com.example.scarlet.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.scarlet.R
import com.example.scarlet.ui.theme.ScarletTheme
import com.example.scarlet.compose.ActiveBlockSection
import com.example.scarlet.compose.CompletedBlocksSection
import com.example.scarlet.db.ScarletDatabase
import com.example.scarlet.db.ScarletRepositoryImpl
import com.example.scarlet.viewmodel.TrainingLogViewModelFactory

class TrainingLogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this
        val blockDao = ScarletDatabase.getInstance(application).blockDao
        val repository = ScarletRepositoryImpl(blockDao)
        val factory = TrainingLogViewModelFactory(repository)
        setContent {
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
                            fontSize = 48.sp)
                        ActiveBlockSection(context, factory)
                        CompletedBlocksSection(context, factory)
                    }
                }
            }
        }
    }
}