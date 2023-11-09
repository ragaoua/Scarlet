package com.example.scarlet.feature_training_log.domain.use_case.movement

import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.toTitleCase
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class FormatNewMovementNameUseCase(
    val repository: ScarletRepository
) {

    /**
     * Format a movement name to be added to the database.
     * The name is trimmed and converted to title case.
     * If the name is blank or if it already exists, null is returned.
     *
     * @param name movement name
     * @param movements list of existing movements
     *
     * @return a resource with data (the formatted movement name) or null
     */
    operator fun invoke(name: String, movements: List<Movement>): Resource<String?> {
        val formattedMovementName = name.trim().toTitleCase()

        if (formattedMovementName.isBlank() || movements.any { it.name == formattedMovementName }) {
            return Resource.Success(null)
        }

        return Resource.Success(formattedMovementName)
    }
}