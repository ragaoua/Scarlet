package com.example.scarlet.ui.navigation

sealed class Screen(val route: String) {
    object HomeScreen: Screen("home_screen")
    object TrainingLogScreen: Screen("training_log_screen")
    object BlockScreen: Screen("block_screen/{id}")
    object SessionScreen: Screen("sessionScreen/{id}")

    fun withId(id: Int): String {
        return this.route.replace("{id}", id.toString())
    }

}
