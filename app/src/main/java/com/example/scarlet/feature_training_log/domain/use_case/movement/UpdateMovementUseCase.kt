package com.example.scarlet.feature_training_log.domain.use_case.movement

import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.SimpleResource
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.use_case.movement.helpers.ValidateMovementNameHelper

class UpdateMovementUseCase(
    private val repository: ScarletRepository,
    private val validateMovementName: ValidateMovementNameHelper
) {

    /**
     * Update a movement after validating its name
     *
     * @param movement movement to be updated
     *
     * @return a resource with an error if found, or a simple resource with no data
     */
    suspend operator fun invoke(movement: Movement): SimpleResource {
        validateMovementName(movement).let { resource ->
            resource.error?.let {
                return Resource.Error(resource.error)
            }
        }

        repository.updateMovement(movement)
        return Resource.Success()
    }
}