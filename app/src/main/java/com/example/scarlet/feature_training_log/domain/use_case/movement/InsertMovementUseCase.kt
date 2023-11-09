package com.example.scarlet.feature_training_log.domain.use_case.movement

import com.example.scarlet.R
import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.StringResource
import com.example.scarlet.core.util.toTitleCase
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import com.example.scarlet.feature_training_log.domain.use_case.movement.helpers.ValidateMovementNameHelper
import kotlinx.coroutines.flow.first

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

        return repository.getAllMovements().first()
            .let { movements ->
                if (movements.any { it.name.equals(movementName, ignoreCase = true) }) {
                    Resource.Error(StringResource(R.string.error_movement_already_exists))
                } else {
                    repository.insertMovement(
                        Movement(
                            name = movementName.toTitleCase() // TODO : strip string
                        )
                    ).let { Resource.Success(it) }
                }
            }
    }
}