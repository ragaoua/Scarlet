package com.example.scarlet.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.scarlet.compose.BlockHeader
import com.example.scarlet.db.ScarletDatabase
import com.example.scarlet.db.ScarletRepository
import com.example.scarlet.theme.ScarletTheme
import com.example.scarlet.viewmodel.TrainingLogViewModelFactory

class BlockActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = TrainingLogViewModelFactory(
            ScarletRepository(
                ScarletDatabase.getInstance(application)
            )
        )
        setContent {
            ScarletTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BlockHeader(this, factory)
                    //SessionsSection(this, factory)
                }
            }
        }
    }
}