package com.example.scarlet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.scarlet.ui.compose.BlockScreen
import com.example.scarlet.ui.compose.HomeScreen
import com.example.scarlet.ui.compose.SessionScreen
import com.example.scarlet.ui.compose.TrainingLogScreen
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
        composable(
            route = Screen.SessionScreen.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { entry ->
            SessionScreen(entry.arguments?.getInt("id")!!, navController = navController, factory = factory)
        }
    }
}