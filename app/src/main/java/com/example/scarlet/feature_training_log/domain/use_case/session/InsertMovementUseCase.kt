package com.example.scarlet.feature_training_log.domain.use_case.session

import com.example.scarlet.core.util.Resource
import com.example.scarlet.feature_training_log.domain.model.Movement
import com.example.scarlet.feature_training_log.domain.repository.ScarletRepository

class InsertMovementUseCase(
    private val repository: ScarletRepository
) {

    suspend operator fun invoke(movementName: String): Resource<Long> {
        val movement = Movement(
            name = movementName
        )

        return Resource.Success(
            repository.insertMovement(movement)
        )
    }
}