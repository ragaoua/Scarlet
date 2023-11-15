package com.example.scarlet.feature_training_log.domain.use_case.movement

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.use_case.movement.helpers.ValidateMovementNameHelper

class InsertMovementUseCase(
    private val repository: ScarletRepository,
    private val validateMovementName: ValidateMovementNameHelper
) {

    /**
     * Insert a movement after checking it doesn't already exist.
     *
     * @param movementName name of the movement to be inserted
     *
     * @return a resource with an error or data (id of the inserted movement)
     */
    suspend operator fun invoke(movementName: String): Resource<Long> {
        validateMovementName(movementName).let { resource ->
            resource.error?.let {
                return Resource.Error(resource.error)
            }
        }

        return repository.insertMovement(
            Movement(name = movementName)
        ).let { Resource.Success(it) }
    }
}