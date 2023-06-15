package com.example.scarlet.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.scarlet.db.ScarletDatabase
import com.example.scarlet.db.ScarletRepository
import com.example.scarlet.ui.navigation.Navigation
import com.example.scarlet.viewmodel.TrainingLogViewModel
import com.example.scarlet.viewmodel.TrainingLogViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = TrainingLogViewModelFactory(
            ScarletRepository(
                ScarletDatabase.getInstance(application)
            )
        )
        val trainingLogViewModel =
            ViewModelProvider(this, factory)[TrainingLogViewModel::class.java]
        setContent {
            val navController = rememberNavController()
            Navigation(navController, trainingLogViewModel)
        }
    }
}