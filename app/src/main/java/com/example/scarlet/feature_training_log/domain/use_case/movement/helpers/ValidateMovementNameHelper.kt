package com.example.scarlet.feature_training_log.domain.use_case.movement.helpers

import com.example.scarlet.R
import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class ValidateMovementNameHelper(
    private val repository: ScarletRepository
) {

    /**
     * Check that a movement name isn't blank or used already.
     *
     * @param movementName the movement name
     *
     * @return simple resource
     */
    suspend operator fun invoke(movementName: String): SimpleResource {
        if (movementName.isBlank()) {
            return Resource.Error(StringResource(R.string.error_movement_name_is_empty))
        }

        repository.getMovementByName(movementName)?.let { existingMovement ->
            return Resource.Error(
                StringResource(R.string.error_movement_already_exists, existingMovement.name)
            )
        }

        return Resource.Success()
    }

}