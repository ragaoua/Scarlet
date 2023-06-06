package com.example.scarlet

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.scarlet.compose.BlockScreen
import com.example.scarlet.compose.HomeScreen
import com.example.scarlet.compose.TrainingLogScreen
import com.example.scarlet.viewmodel.TrainingLogViewModelFactory

@Composable
fun Navigation(
    navController: NavHostController,
    factory: TrainingLogViewModelFactory
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.TrainingLogScreen.route) {
            TrainingLogScreen(navController = navController, factory = factory)
        }
        composable(
            route = Screen.BlockScreen.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { entry ->
            BlockScreen(entry.arguments?.getInt("id")!!, navController = navController, factory = factory)
        }
    }
}