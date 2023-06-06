package com.example.scarlet.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.scarlet.Navigation
import com.example.scarlet.db.ScarletDatabase
import com.example.scarlet.db.ScarletRepository
import com.example.scarlet.ui.theme.ScarletTheme
import com.example.scarlet.viewmodel.TrainingLogViewModelFactory

class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController
    lateinit var factory: TrainingLogViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = TrainingLogViewModelFactory(
            ScarletRepository(
                ScarletDatabase.getInstance(application)
            )
        )
        setContent {
            ScarletTheme {
                navController = rememberNavController()
                Navigation(navController, factory)
            }
        }
    }
}