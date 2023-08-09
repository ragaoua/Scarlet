package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.R
import com.example.scarlet.core.util.Resource
import com.example.scarlet.core.util.StringResource
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class InsertMovementUseCase(
    private val repository: ScarletRepository
) {

    private var movements: Flow<List<Movement>>? = null

    /**
     * Insert a movement after checking it doesn't already exist.
     *
     * @param movementName name of the movement to be inserted
     *
     * @return a resource with an error or data (id of the inserted movement)
     */
    suspend operator fun invoke(movementName: String): Resource<Long> {
        return (movements ?: run { repository.getAllMovements() })
            .also { movements = it }
            .map { movements ->
                if (movements.any { it.name.equals(movementName, ignoreCase = true) }) {
                    Resource.Error(StringResource(R.string.error_movement_already_exists))
                } else {
                    repository.insertMovement(Movement(name = movementName))
                        .let { Resource.Success(it) }
                }
            }
            .first()
    }
}