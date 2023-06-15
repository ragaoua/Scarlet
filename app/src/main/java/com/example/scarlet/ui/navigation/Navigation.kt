package com.example.scarlet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.scarlet.ui.screen.BlockScreen
import com.example.scarlet.ui.screen.ExerciseScreen
import com.example.scarlet.ui.screen.HomeScreen
import com.example.scarlet.ui.screen.SessionScreen
import com.example.scarlet.ui.screen.TrainingLogScreen
import com.example.scarlet.viewmodel.TrainingLogViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    trainingLogViewModel: TrainingLogViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.TrainingLogScreen.route) {
            TrainingLogScreen(navController = navController, trainingLogViewModel = trainingLogViewModel)
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
            BlockScreen(entry.arguments?.getInt("id")!!, navController = navController, trainingLogViewModel = trainingLogViewModel)
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
            SessionScreen(entry.arguments?.getInt("id")!!, navController = navController, trainingLogViewModel = trainingLogViewModel)
        }
        composable(
            route = Screen.ExerciseScreen.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) {entry ->
            ExerciseScreen(entry.arguments?.getInt("id")!!, navController = navController, trainingLogViewModel = trainingLogViewModel)

        }
    }
}