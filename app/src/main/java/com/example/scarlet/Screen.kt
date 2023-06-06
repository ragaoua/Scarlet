package com.example.scarlet

sealed class Screen(val route: String) {
    object HomeScreen: Screen("home_screen")
    object TrainingLogScreen: Screen("training_log_screen")
    object BlockScreen: Screen("block_screen/{id}") {
        fun withId(id: Int): String {
            return this.route.replace("{id}", id.toString())
        }
    }
}
